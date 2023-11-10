package site.sorghum.anno.anno.annotation.field;

import site.sorghum.anno.anno.annotation.field.type.AnnoImageType;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.anno.enums.AnnoDataType;

import java.lang.annotation.*;

/**
 * Anno字段注解
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface AnnoField {

    /**
     * 标题
     *
     * @return {@link String}
     */
    String title();

    /**
     * 表字段名
     * 如果不设置，则默认其为虚拟列，不会在数据库中生成与查询
     *
     * @return {@link String}
     */
    String tableFieldName() default "";

    /**
     * 数据库中的字段长度，为 0 时使用 anno 设置的默认长度
     */
    int fieldSize() default 0;

    /**
     * 字段默认值，比如 0
     */
    String defaultValue() default "";

    /**
     * 显示
     *
     * @return boolean
     */
    boolean show() default true;

    /**
     * 搜索信息
     *
     * @return {@link AnnoSearch}
     */
    AnnoSearch search() default @AnnoSearch(enable = false);

    /**
     * 编辑信息
     * @return {@link AnnoEdit}
     */
    AnnoEdit edit() default @AnnoEdit(editEnable = false, addEnable = false);

    /**
     * 数据类型
     */
    AnnoDataType dataType() default AnnoDataType.STRING;

    /**
     * 选择类型
     *
     * @return {@link AnnoOptionType}
     */
    AnnoOptionType optionType() default @AnnoOptionType;

    /**
     * 图像类型
     *
     * @return {@link AnnoImageType}
     */
    AnnoImageType imageType() default @AnnoImageType;

    /**
     * 选择类型-树
     *
     * @return {@link AnnoTreeType}
     */
    AnnoTreeType treeType() default @AnnoTreeType;

    /**
     * 是否是虚拟列，不会在数据库中生成与查询
     * 可不绑定虚拟表独立使用
     */
    boolean virtualColumn() default false;
}
