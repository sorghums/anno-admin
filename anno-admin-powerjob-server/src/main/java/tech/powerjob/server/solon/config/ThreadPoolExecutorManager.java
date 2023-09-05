package tech.powerjob.server.solon.config;

import lombok.Getter;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.bean.LifecycleBean;
import org.noear.solon.core.util.NamedThreadFactory;
import tech.powerjob.server.solon.common.RejectedExecutionHandlerFactory;
import tech.powerjob.server.solon.common.constants.PJThreadPool;
import tech.powerjob.server.solon.common.thread.NewThreadRunRejectedExecutionHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理
 *
 * @author songyinyin
 * @since 2023/8/27 15:07
 */
@Getter
@Component
public class ThreadPoolExecutorManager implements LifecycleBean {

    private ThreadPoolExecutor timingThreadPool;

    private ThreadPoolExecutor backgroundThreadPool;

    private ThreadPoolExecutor localDbThreadPool;

    @Override
    public void start() throws Throwable {
        int availableProcessors = Runtime.getRuntime().availableProcessors();

        timingThreadPool = new ThreadPoolExecutor(availableProcessors, availableProcessors * 4,
            60, TimeUnit.SECONDS, createQueue(0), new NamedThreadFactory("PJ-TIMING-"),
            new NewThreadRunRejectedExecutionHandler(PJThreadPool.TIMING_POOL));

        backgroundThreadPool = new ThreadPoolExecutor(availableProcessors * 8, availableProcessors * 16,
            60, TimeUnit.SECONDS, createQueue(8192), new NamedThreadFactory("PJ-BG-"),
            RejectedExecutionHandlerFactory.newDiscard(PJThreadPool.BACKGROUND_POOL));

        int tSize = Math.max(1, availableProcessors / 2);
        localDbThreadPool = new ThreadPoolExecutor(tSize, tSize,
            60, TimeUnit.SECONDS, createQueue(2048), new NamedThreadFactory("PJ-LOCALDB-"),
            RejectedExecutionHandlerFactory.newAbort(PJThreadPool.LOCAL_DB_POOL));
    }

    /**
     * 创建队列：如果队列容量大于0，则创建有界队列（LinkedBlockingQueue），否则创建无界队列（SynchronousQueue）
     *
     * @param queueCapacity 队列容量
     */
    protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
        if (queueCapacity > 0) {
            return new LinkedBlockingQueue<>(queueCapacity);
        } else {
            return new SynchronousQueue<>();
        }
    }
}
