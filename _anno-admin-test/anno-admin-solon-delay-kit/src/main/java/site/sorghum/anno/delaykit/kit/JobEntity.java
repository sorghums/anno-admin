package site.sorghum.anno.delaykit.kit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.noear.snack.ONode;

import java.util.Date;

/**
 * 任务
 *
 * @author Sorghum
 * @since 2023/07/24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobEntity {
    /**
     * 目标处理类
     */
    String targetProcessClass;

    /**
     * 执行几次
     */
    @Builder.Default
    int nowCount = 0;

    /**
     * 最大执行次数
     */
    @Builder.Default
    int maxCount = 2000;

    /**
     * 间隔时间【秒】
     */
    int interval = 30;

    /**
     * 上次运行时间
     */
    Date lastRunTime;

    /**
     * 首次运行时间
     */
    Date firstRunTime;

    /**
     * 运行时间
     */
    Date runTime;

    /**
     * 数据
     */
    String data;

    /**
     * 将当前对象中的数据转换为指定类型的对象
     *
     * @param clazz 目标类型的Class对象
     * @param <T>   目标类型的泛型标识
     * @return 转换后的对象
     */
    public <T> T dataTo(Class<T> clazz) {
        return ONode.deserialize(data, clazz);
    }

    /**
     * 将传入的对象序列化后存入data变量中
     *
     * @param obj 需要被序列化的对象
     */
    public void dataIn(Object obj) {
        data = ONode.serialize(obj);
    }
}
