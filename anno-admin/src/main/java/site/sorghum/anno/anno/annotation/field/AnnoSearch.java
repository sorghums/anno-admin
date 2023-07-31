package site.sorghum.anno.anno.annotation.field;

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
