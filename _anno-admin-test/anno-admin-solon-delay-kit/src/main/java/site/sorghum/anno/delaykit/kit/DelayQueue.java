package site.sorghum.anno.delaykit.kit;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.List;

/**
 * 延迟队列
 *
 * @author Sorghum
 * @since 2023/07/24
 */
@Getter
@Component
@Slf4j
public class DelayQueue {
    private static final String PREFIX = "delay-kit:delay-queue:";

    List<RDelayedQueue<String>> delayedQueues = new ArrayList<>();

    List<RBlockingQueue<String>> blockingQueues = new ArrayList<>();

    RBlockingQueue<Object> tempBlockingQueue;


    public DelayQueue( ) {
        Solon.context().getBeanAsync(RedissonClient.class,redissonClient->{
            log.info("初始化延迟队列");
            tempBlockingQueue = redissonClient.getBlockingQueue(PREFIX + "-1");
            // 预设8个延迟队列
            for (int i = 0; i < 8; i++) {
                RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue(PREFIX + i);
                RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
                delayedQueues.add(delayedQueue);
                blockingQueues.add(blockingQueue);
            }
            new JobListener();
        });
    }


    public RDelayedQueue<String> getDelayedQueue(int idx){
        if (idx < 0 || idx > 7) {
            throw new IllegalArgumentException("idx must be between 0 and 7");
        }
        return delayedQueues.get(idx);
    }

}
