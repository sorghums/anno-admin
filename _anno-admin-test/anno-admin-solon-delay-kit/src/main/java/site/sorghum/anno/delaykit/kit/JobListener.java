package site.sorghum.anno.delaykit.kit;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.redisson.api.RBlockingQueue;
import site.sorghum.anno._common.AnnoBeanUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 工作侦听器
 *
 * @author Sorghum
 * @since 2023/07/24
 */
@Slf4j
public class JobListener {

    public JobListener() {
        boolean asyncUpdate = Solon.cfg().getBool("kit.server", false);
        if (!asyncUpdate) {
            return;
        }
        Solon.context().getBeanAsync(DelayQueue.class,
            delayQueue -> {
                Solon.context().getBeanAsync(DelayJobUtil.class,
                    delayJobUtil -> {
                        log.info("🚀🚀🚀🚀 ==> JobListener 启动成功！");
                        List<RBlockingQueue<String>> blockingQueues = delayQueue.getBlockingQueues();
                        // 每个delayedQueue创建4个线程的线程池
                        int cnt = 0;
                        int threadCount = 4;
                        for (RBlockingQueue<String> blockingQueue : blockingQueues) {
                            ExecutorService executorService = ThreadUtil.newFixedExecutor(threadCount, "delayedQueue-" + cnt + "-", true);
                            Runnable runnable = () -> {
                                while (true) {
                                    try {
                                        String polled = blockingQueue.take();
                                        if (polled.contains(":::")) {
                                            String[] split = polled.split(":::");
                                            polled = split[split.length - 1];
                                        }
                                        JobEntity jobEntity = ONode.deserialize(polled, JobEntity.class);
                                        if (jobEntity.getNowCount() >= jobEntity.getMaxCount()) {
                                            return;
                                        }
                                        Date now = new Date();
                                        jobEntity.setNowCount(jobEntity.getNowCount() + 1);
                                        jobEntity.setLastRunTime(jobEntity.getRunTime());
                                        if (jobEntity.getFirstRunTime() == null) {
                                            jobEntity.setFirstRunTime(now);
                                        }
                                        jobEntity.setRunTime(now);
                                        Class<?> jobClazz;
                                        try {
                                            jobClazz = Class.forName(jobEntity.getTargetProcessClass());
                                        } catch (Exception ignored) {
                                            return;
                                        }
                                        BaseJob baseJob = (BaseJob) AnnoBeanUtils.getBean(jobClazz);
                                        try {
                                            baseJob.run(jobEntity);
                                        } catch (Exception exception) {
                                            log.warn("JobListener:{},has reOffer job", exception.getMessage());
                                            DelayJobUtil.offer(jobEntity, null, jobEntity.getInterval(), jobEntity.getMaxCount());
                                        }
                                    } catch (InterruptedException interruptedException) {
                                        log.error("JobListener:{},has error", interruptedException.getMessage());
                                    }
                                }
                            };
                            submitTaskFixedTime(runnable, executorService, threadCount);
                            cnt++;
                        }

                    });
            }
        );

    }

    public void submitTaskFixedTime(Runnable runnable, ExecutorService executorService, int count) {
        for (int i = 0; i < count; i++) {
            executorService.submit(
                runnable
            );
        }
    }
}
