package site.sorghum.anno.method;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * @author songyinyin
 * @since 2024/1/16 16:31
 */
@Data
public class MethodTemplateCsv {

    /**
     * 顺序位，越小越先执行
     */
    private double index;

    private String beanName;

    private String methodName;

    /**
     * 是否排除
     */
    private boolean exclude;

    /**
     * 执行的条件表达式(Aviator)，方法的参数可以用p0,p1...表示，比如 mt.instanceofBaseMetaModel(p0)
     */
    private String condition;

    public String getBeanMethodName() {
        if (StrUtil.isBlank(methodName)) {
            return beanName;
        }
        return "%s#%s()".formatted(beanName, methodName);
    }
}
