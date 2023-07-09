package site.sorghum.anno.modular.anno.annotation.field;

import site.sorghum.anno.modular.anno.annotation.field.type.AnnoImageType;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;

import java.lang.annotation.*;

/**
 * Anno字段注解
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
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
     *
     * @return {@link String}
     */
    String tableFieldName();

    /**
     * 数据库中的字段长度，为 0 时使用 anno 设置的默认长度
     */
    int fieldSize() default 0;

    /**
     * 定义数据库中的默认值，比如：DEFAULT 0
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
     * 选择类型
     *
     * @return {@link AnnoOptionType}
     */
    AnnoTreeType treeType() default @AnnoTreeType;
}
