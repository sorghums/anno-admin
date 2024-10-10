package site.sorghum.anno.delaykit.kit;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.redisson.api.RDelayedQueue;

import java.util.concurrent.TimeUnit;

/**
 * 工作工具类
 *
 * @author Sorghum
 * @since 2023/07/24
 */
@Component
public class DelayJobUtil {

    private static DelayQueue delayQueue;

    public DelayJobUtil() {
        Solon.context().getBeanAsync(
            DelayQueue.class,
            delayQueue -> DelayJobUtil.delayQueue = delayQueue
        );
    }

    /**
     * 提供任务 => 执行成功自动停止 => 尝试最大次数【2000】后停止
     */
    public static void offer(Object data, Class<? extends BaseJob> jobClazz) {
        offer(data, jobClazz, 30, 3);
    }


    /**
     * 提供任务 => 执行成功自动停止 => 尝试最大次数【2000】后停止，
     */
    public static void offer(Object data, Class<? extends BaseJob> jobClazz, int seconds) {
        offer(data, jobClazz, seconds, 3);
    }


    /**
     * 提供任务 => 执行成功自动停止 => 尝试最大次数【2000】后停止
     */
    @SneakyThrows
    public static void offer(Object data, Class<? extends BaseJob> jobClazz, int interval, int maxCount) {
        JobEntity jobEntity;
        if (data.getClass() == JobEntity.class) {
            jobEntity = (JobEntity) data;
            jobClazz = (Class<? extends BaseJob>) Class.forName(jobEntity.getTargetProcessClass());
        } else {
            jobEntity = new JobEntity();
            jobEntity.setTargetProcessClass(jobClazz.getName());
            if (data instanceof String) {
                jobEntity.setData((String) data);
            } else {
                jobEntity.setData(JSON.toJSONString(data));
            }
        }
        jobEntity.setInterval(interval);
        jobEntity.setMaxCount(maxCount);
        int randomInt = RandomUtil.randomInt(0, delayQueue.getDelayedQueues().size());
        delayQueue.getDelayedQueue(randomInt).offer(jobClazz.getName() + ":::" + ONode.serialize(data) + ":::" + ONode.serialize(jobEntity), interval, TimeUnit.SECONDS);
    }

    /**
     * 删除旧的任务
     */
    public static boolean removeOld(Class<? extends BaseJob> jobClazz) {
        boolean result = false;
        for (RDelayedQueue<String> delayedQueue : delayQueue.getDelayedQueues()) {
            result = delayedQueue.removeIf(d -> d.startsWith(jobClazz.getName() + ":::")) || result;
        }
        return result;
    }


    /**
     * 删除旧的任务
     */
    public static boolean removeOld(Class<? extends BaseJob> jobClazz, Object data) {
        boolean result = false;
        for (RDelayedQueue<String> delayedQueue : delayQueue.getDelayedQueues()) {
            result = delayedQueue.removeIf(d -> d.startsWith(jobClazz.getName() + ":::" + ONode.serialize(data) + ":::")) || result;
        }
        return result;
    }

}
