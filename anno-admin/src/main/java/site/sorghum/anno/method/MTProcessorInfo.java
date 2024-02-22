package site.sorghum.anno.method;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.Objects;

/**
 * 方法部件的相关信息
 *
 * @author songyinyin
 * @since 2024/1/17 18:19
 */
@Data
public class MTProcessorInfo {

    /**
     * 执行阶段
     */
    private ExecutePhase phase;

    /**
     * 顺序位，越小越先执行
     */
    private double index;

    /**
     * 指定当前部件的 beanName
     */
    private String beanName;

    private Object bean;
    /**
     * 指定当前部件的 methodName
     */
    private String methodName;

    /**
     * 是否排除
     */
    private boolean exclude;

    /**
     * 执行的条件表达式(Aviator)，方法的参数可以用 p0,p1 ... 表示，比如 mt.instanceofBaseMetaModel(p0)
     */
    private String condition;

    /**
     * 规则文件key
     */
    private String fullPath;

    /**
     * 进行 方法模版处理 的方法
     */
    private String mtMethodName;

    public String getBeanMethodName() {
        if (StrUtil.isBlank(methodName)) {
            return beanName;
        }
        return "%s#%s()".formatted(beanName, methodName);
    }

    public void deal() {
        if (StrUtil.isBlank(methodName)) {
            methodName = null;
        }
        if (StrUtil.isBlank(condition)) {
            condition = null;
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MTProcessorInfo info = (MTProcessorInfo) object;
        return exclude == info.exclude && Objects.equals(beanName, info.beanName) && Objects.equals(methodName, info.methodName) && Objects.equals(condition, info.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanName, methodName, exclude, condition);
    }
}
