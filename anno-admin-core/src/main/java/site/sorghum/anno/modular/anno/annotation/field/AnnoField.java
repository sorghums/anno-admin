package site.sorghum.anno.modular.anno.annotation.field;

import site.sorghum.anno.modular.anno.annotation.field.type.AnnoImageType;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoOptionType;
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
     * 是否为主键
     *
     * @return boolean
     */
    boolean isId() default false;

    /**
     * 表字段名
     *
     * @return {@link String}
     */
    String tableFieldName();

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
}
