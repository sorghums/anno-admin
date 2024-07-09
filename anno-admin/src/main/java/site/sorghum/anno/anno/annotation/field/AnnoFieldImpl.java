package site.sorghum.anno.anno.annotation.field;

import lombok.*;
import site.sorghum.anno.anno.annotation.field.type.*;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.proxy.field.EmptyFieldBaseSupplier;
import site.sorghum.anno.anno.proxy.field.FieldBaseSupplier;

import java.lang.annotation.*;

/**
 * Anno字段注解
 *
 * @author sorghum
 * @since 2024/07/04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnoFieldImpl implements AnnoField {
    /**
     * 标题
     */
    String title;

    /**
     * 表字段名
     * 如果不设置，则默认使用驼峰式转下划线
     */
    String tableFieldName = "";

    /**
     * 数据库中的字段长度，为 0 时使用 anno 设置的默认长度
     */
    int fieldSize = 0;

    /**
     * 显示
     */
    boolean show = true;

    /**
     * 搜索信息
     */
    AnnoSearchImpl search = AnnoSearchImpl.builder().enable(false).build();

    /**
     * 编辑信息
     */
    AnnoEditImpl edit = AnnoEditImpl.builder().editEnable(false).addEnable(false).build();

    /**
     * 数据类型
     */
    AnnoDataType dataType = AnnoDataType.STRING;

    /**
     * 选择类型
     */
    AnnoOptionTypeImpl optionType = new AnnoOptionTypeImpl();

    /**
     * 代码类型
     */
    AnnoCodeType codeType = new AnnoCodeTypeImpl();

    /**
     * 图像类型
     */
    AnnoImageTypeImpl imageType = new AnnoImageTypeImpl();

    /**
     * 选择类型-树
     */
    AnnoTreeTypeImpl treeType = new AnnoTreeTypeImpl();

    /**
     * 文件类型
     */
    AnnoFileTypeImpl fileType = new AnnoFileTypeImpl();

    /**
     * 是否是虚拟列，不会在数据库中生成与查询
     * 可不绑定虚拟表独立使用
     */
    boolean virtualColumn = false;

    /**
     * 是否是主键,默认为false
     * 在使用重载方法时，@PrimaryKey注解无法使用，需要使用此注解
     */
    boolean pkField = false;

    /**
     * 字段排序，越大优先级越高
     * 使用在表格列、表单列中等
     */
    int sort = 0;

    /**
     * 更新为null时设置值
     */
    Class<? extends FieldBaseSupplier> updateWhenNullSet = EmptyFieldBaseSupplier.class;

    /**
     * 插入为null时设置值
     */
    Class<? extends FieldBaseSupplier> insertWhenNullSet = EmptyFieldBaseSupplier.class;


    @Override
    public String title() {
        return this.title;
    }

    @Override
    public String tableFieldName() {
        return this.tableFieldName;
    }

    @Override
    public int fieldSize() {
        return this.fieldSize;
    }

    @Override
    public boolean show() {
        return this.show;
    }

    @Override
    public AnnoSearch search() {
        return this.search;
    }

    @Override
    public AnnoEdit edit() {
        return this.edit;
    }

    @Override
    public AnnoDataType dataType() {
        return this.dataType;
    }

    @Override
    public AnnoOptionType optionType() {
        return this.optionType;
    }

    @Override
    public AnnoImageType imageType() {
        return this.imageType;
    }

    @Override
    public AnnoCodeType codeType() {
        return this.codeType;
    }

    @Override
    public AnnoTreeType treeType() {
        return this.treeType;
    }

    @Override
    public AnnoFileType fileType() {
        return this.fileType;
    }

    @Override
    public boolean virtualColumn() {
        return this.virtualColumn;
    }

    @Override
    public boolean pkField() {
        return this.pkField;
    }

    @Override
    public int sort() {
        return this.sort;
    }

    @Override
    public Class<? extends FieldBaseSupplier> updateWhenNullSet() {
        return this.updateWhenNullSet;
    }

    @Override
    public Class<? extends FieldBaseSupplier> insertWhenNullSet() {
        return this.insertWhenNullSet;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoField.class;
    }
}
