package site.sorghum.anno.test.modular.ebusiness;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.modular.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoImageType;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.base.model.BaseMetaModel;

/**
 * 商品分类
 *
 * @author Sorghum
 * @since 2023/07/03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "商品类别",
        annoTree = @AnnoTree(parentKey = "parentId", key = "id", displayAsTree = true, label = "catName"))
@Table("business_product_cat")
public class BusinessProductCat extends BaseMetaModel {
    @AnnoField(
            title = "分类图片",
            tableFieldName = "cat_image",
            dataType = AnnoDataType.IMAGE,
            imageType = @AnnoImageType(),
            edit = @AnnoEdit
    )
    String catImage;

    @AnnoField(
            title = "分类名称",
            tableFieldName = "cat_name",
            edit = @AnnoEdit
    )
    String catName;

    @AnnoField(title = "排序",
            tableFieldName = "sort",
            edit = @AnnoEdit
    )
    Integer sort;


    @AnnoField(
            title = "父级分类",
            tableFieldName = "parent_id",
            edit = @AnnoEdit
    )
    String parentId;
}