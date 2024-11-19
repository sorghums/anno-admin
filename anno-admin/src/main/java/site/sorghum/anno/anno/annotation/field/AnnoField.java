package site.sorghum.anno.anno.annotation.field;

import site.sorghum.anno.anno.annotation.field.type.*;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.proxy.field.EmptyFieldBaseSupplier;
import site.sorghum.anno.anno.proxy.field.FieldBaseSupplier;

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
     * 如果不设置，则默认使用驼峰式转下划线
     *
     * @return {@link String}
     */
    String tableFieldName() default "";

    /**
     * 数据库中的字段长度，为 0 时使用 anno 设置的默认长度
     */
    int fieldSize() default 0;

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
     * 代码类型
     *
     * @return {@link AnnoCodeType}
     */
    AnnoCodeType codeType() default @AnnoCodeType;
    /**
     * 选择类型-树
     *
     * @return {@link AnnoTreeType}
     */
    AnnoTreeType treeType() default @AnnoTreeType;

    /**
     * 文件类型
     *
     * @return {@link AnnoFileType}
     */
    AnnoFileType fileType() default @AnnoFileType;

    /**
     * 是否是虚拟列，不会在数据库中生成与查询
     * 可不绑定虚拟表独立使用
     */
    boolean virtualColumn() default false;

    /**
     * 是否是主键,默认为false
     * 在使用重载方法时，@PrimaryKey注解无法使用，需要使用此注解
     */
    boolean pkField() default false;

    /**
     * 字段排序，越大优先级越高
     * 使用在表格列、表单列中等
     *
     * @return long
     */
    int sort() default 0;

    /**
     * 更新为null时设置值
     *
     * @return {@link Class}<{@link ?}>
     */
    Class<? extends FieldBaseSupplier> updateWhenNullSet() default EmptyFieldBaseSupplier.class;

    /**
     * 插入为null时设置值
     *
     * @return {@link Class}<{@link ?}>
     */
    Class<? extends FieldBaseSupplier> insertWhenNullSet() default EmptyFieldBaseSupplier.class;

    /**
     * 是否不翻译
     * @return boolean
     */
    boolean noTranslate() default false;
}
