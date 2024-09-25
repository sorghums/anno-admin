package site.sorghum.anno.anno.annotation.field;

import site.sorghum.anno.anno.proxy.field.EmptyFieldBaseSupplier;
import site.sorghum.anno.anno.proxy.field.FieldBaseSupplier;
import site.sorghum.anno.db.QueryType;

import java.lang.annotation.*;

/**
 * Anno搜索
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
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
     * 查询类型
     *
     * @return {@link QueryType}
     */
    QueryType queryType() default QueryType.EQ;

    /**
     * 提示信息
     *
     * @return {@link String}
     */
    String placeHolder() default "";

    /**
     * 搜索默认值
     * @return {@link String}
     */
    String defaultValue() default "";

    /**
     * 搜索默认值 提供器
     *
     * @return {@link Class}<{@link ?}>
     */
    Class<? extends FieldBaseSupplier> defaultValueSupplier() default EmptyFieldBaseSupplier.class;
}
