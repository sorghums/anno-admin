package tech.powerjob.server.solon.common.async;

import org.noear.solon.core.AppContext;
import org.noear.solon.core.aspect.Interceptor;
import org.noear.solon.core.aspect.Invocation;
import tech.powerjob.common.exception.PowerJobException;
import tech.powerjob.server.solon.common.constants.PJThreadPool;
import tech.powerjob.server.solon.config.ThreadPoolExecutorManager;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author songyinyin
 * @since 2023/8/27 15:35
 */
public class PjAsyncInterceptor implements Interceptor {

    ThreadPoolExecutorManager threadPoolExecutorManager;

    public PjAsyncInterceptor(AppContext context) {
        context.getBeanAsync(ThreadPoolExecutorManager.class, bean -> {
            threadPoolExecutorManager = bean;
        });
    }

    @Override
    public Object doIntercept(Invocation inv) throws Throwable {
        PjAsync anno = inv.method().getAnnotation(PjAsync.class);

        if (anno != null) {
            InvocationCallable callable = new InvocationCallable(inv);
            Future<?> future;
            switch (anno.value()) {
                case PJThreadPool.TIMING_POOL ->
                    future = threadPoolExecutorManager.getTimingThreadPool().submit(callable);
                case PJThreadPool.BACKGROUND_POOL -> future =
                    threadPoolExecutorManager.getBackgroundThreadPool().submit(callable);
                case PJThreadPool.LOCAL_DB_POOL ->
                    future = threadPoolExecutorManager.getLocalDbThreadPool().submit(callable);
                default -> throw new PowerJobException("unknown thread pool name: " + anno.value());
            }
            if (inv.method().getReturnType().isAssignableFrom(Future.class)) {
                return future;
            } else {
                return null;
            }
        } else {
            return inv.invoke();
        }
    }

    public static class InvocationCallable implements Callable<Object> {

        protected Invocation invocation;

        public InvocationCallable(Invocation inv) {
            invocation = inv;
        }

        @Override
        public Object call() throws Exception {
            try {
                Object result = invocation.invoke();
                if (result instanceof Future) {
                    return ((Future<?>) result).get();
                }
                return null;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }
}
