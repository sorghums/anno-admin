package site.sorghum.anno.method;

import java.lang.annotation.*;

/**
 * 方法执行的单元，一个方法模板包含多个方法单元
 * <p>执行顺序 before -> execute -> after，在相同阶段内，按照index从小到大执行</p>
 *
 * @author songyinyin
 * @since 2024/1/29 20:48
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface MethodUnit {

    /**
     * 方法单元的执行阶段
     */
    ExecutePhase phase() default ExecutePhase.EXECUTE;

    /**
     * 方法单元的执行顺序，在相同阶段内，按照index从小到大执行
     */
    double index() default MethodTemplateManager.MT_DEFAULT_INDEX;
}
