package site.sorghum.anno.test.modular.ebusiness;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoImageType;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.db.QueryType;
import site.sorghum.anno.suppose.model.BaseMetaModel;

/**
 * 商品分类
 *
 * @author Sorghum
 * @since 2023/07/03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "商品类别",
    annoTree = @AnnoTree(parentKey = "parentId", key = "id", label = "catName", displayAsTree = true))
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
        edit = @AnnoEdit,
        search = @AnnoSearch(queryType = QueryType.EQ),
        dataType = AnnoDataType.TREE,
        treeType = @AnnoTreeType(treeAnno = @AnnoTreeType.TreeAnnoClass(annoClass = BusinessProductCat.class, idKey = "id", pidKey = "parentId", labelKey = "catName"))
    )
    String parentId;

    /**
     * 角色按钮
     */
    @AnnoButton(name = "分类商品多对多", m2mJoinButton = @AnnoButton.M2MJoinButton(
        joinTargetClazz = BusinessVirtualTable.class,
        mediumTableClass = BusinessCatProduct.class,
        mediumTargetField = "productId",
        mediumThisField = "catId",
        joinThisClazzField = "id"
    ))
    private Object roleButton;
}
