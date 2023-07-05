package site.sorghum.anno.test.modular.ebusiness;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoLeftTree;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.base.model.BaseMetaModel;

/**
 * 电商商品
 *
 * @author Sorghum
 * @since 2023/07/04
 */
@Data @EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "商品",
         annoLeftTree = @AnnoLeftTree(catKey = "product_cat_id", treeClass = BusinessProductCat.class)
)
@Table("business_product")
public class BusinessProduct extends BaseMetaModel {

    @AnnoField(
            title = "商品名称",
            tableFieldName = "product_name",
            search = @AnnoSearch,
            edit = @AnnoEdit)
    String productName;

    @AnnoField(
            title = "商品图片",
            tableFieldName = "product_image",
            dataType = AnnoDataType.IMAGE,
            edit = @AnnoEdit)
    String productImage;

    @AnnoField(
            title = "商品分类",
            tableFieldName = "product_cat_id",
            dataType = AnnoDataType.OPTIONS,
            optionType = @AnnoOptionType(sql = "SELECT id as value,cat_name as label FROM business_product_cat where del_flag = 0"),
            search = @AnnoSearch,
            edit = @AnnoEdit)
    String productCatId;

    @AnnoField(
            title = "商品描述",
            tableFieldName = "product_desc",
            edit = @AnnoEdit)
    String productDesc;

    @AnnoField(
            title = "商品状态",
            tableFieldName = "product_status",
            dataType = AnnoDataType.OPTIONS,
            optionType = @AnnoOptionType(
                    value = {
                            @AnnoOptionType.OptionData(value = "0", label = "下架"),
                            @AnnoOptionType.OptionData(value = "1", label ="上架")
                    }
            ),
            search = @AnnoSearch,
            edit = @AnnoEdit)
    Integer productStatus;

    @AnnoField(
            title = "运费",
            tableFieldName = "product_freight",
            edit = @AnnoEdit)
    Long productFreight;
}
