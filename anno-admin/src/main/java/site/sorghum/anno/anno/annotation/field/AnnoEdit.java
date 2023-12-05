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
    int span() default 24;

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
     * 展示依赖条件
     * @return {@link ShowBy}
     */
    ShowBy[] showBy() default {@ShowBy(enable = false)};

    /**
     * 展示方式注解
     */
    public @interface ShowBy{
        /**
         * 是否启用展示
         */
        boolean enable() default true;
        /**
         * 当满足指定条件时才展示
         */
        ShowIf[] showIf() default {};
        /**
         * 条件之间的逻辑关系（与 or 或）
         */
        String andOr() default "and";
    }

    /**
     * 展示条件注解
     */
    public @interface ShowIf{
        /**
         * 依赖的字段
         */
        String dependField() default "id";
        /**
         * 展示条件表达式
         */
        String expr() default "value == 0";
        /**
         * 条件之间的逻辑关系（与 or 或）
         */
        String andOr() default "and";
    }

}
