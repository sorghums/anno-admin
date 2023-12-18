package site.sorghum.anno.anno.annotation.field;

import java.lang.annotation.*;

/**
 * Anno编辑
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
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
     * 编辑跨度
     * 默认为 24 [1-24] 24为整行
     *
     * @return int
     */
    int span() default 0;

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

    /**
     * 是否可以清空,一般情况下updateById不会设置为空.
     *
     * @return boolean
     */
    boolean canClear() default false;

    /**
     * 展示依赖条件
     * @return {@link ShowBy}
     */
    ShowBy showBy() default @ShowBy(enable = false);

    /**
     * 展示方式注解
     */
    public @interface ShowBy{
        /**
         * 是否启用展示
         */
        boolean enable() default true;
        /**
         * 展示条件表达式
         * 必须以：annoDataForm.xx 为变量的取值
         */
        String expr() default "annoDataForm.id == 0";
    }

}
