package site.sorghum.anno.solon.interceptor;

import jakarta.transaction.Transactional;
import org.noear.solon.core.aspect.Interceptor;
import org.noear.solon.core.aspect.Invocation;
import org.noear.solon.data.tran.TranUtils;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author songyinyin
 * @since 2023/7/30 21:02
 */
public class TransactionalInterceptor implements Interceptor {
    @Override
    public Object doIntercept(Invocation inv) throws Throwable {
        AtomicReference<Object> val0 = new AtomicReference<>();

        Transactional annotation = inv.method().getAnnotation(Transactional.class);
        if (annotation == null) {
            return inv.invoke();
        }
        TranUtils.execute(new TranAnno(annotation), () -> val0.set(inv.invoke()));

        return val0.get();
    }
}
