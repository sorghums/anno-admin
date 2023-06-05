package site.sorghum.anno.modular.anno.annotation.field;

import java.lang.annotation.*;

/**
 * Anno编辑
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface AnnoEdit {
    /**
     * 是否必填
     *
     * @return boolean
     */
    boolean notNull() default false;

    /**
     * 提示信息
     *
     * @return {@link String}
     */
    String placeHolder() default "";


    /**
     * 新增启用
     *
     * @return boolean
     */
    boolean addEnable() default true;

    /**
     * 编辑启用
     *
     * @return boolean
     */
    boolean editEnable() default true;
}
