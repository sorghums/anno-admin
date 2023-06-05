package site.sorghum.anno.modular.anno.annotation.field;

import java.lang.annotation.*;

/**
 * Anno搜索
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface AnnoSearch {


    /**
     * 是否必填
     *
     * @return boolean
     */
    boolean notNull() default false;

    /**
     * 是否显示
     *
     * @return boolean
     */
    boolean show() default true;

    /**
     * 启用
     *
     * @return boolean
     */
    boolean enable() default true;


    /**
     * 提示信息
     *
     * @return {@link String}
     */
    String placeHolder() default "";
}
