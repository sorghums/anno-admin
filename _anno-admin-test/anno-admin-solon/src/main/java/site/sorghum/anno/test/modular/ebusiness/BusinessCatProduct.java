package site.sorghum.anno.test.modular.ebusiness;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.db.BaseMetaModel;

import java.io.Serializable;

/**
 * 系统角色用户关联表
 *
 * @author Sorghum
 * @since 2023/06/08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "商品分类关联管理")
@Table("business_cat_product_relation")
public class BusinessCatProduct extends BaseMetaModel implements Serializable {

    /**
     * 用户ID
     */
    @AnnoField(title = "分类ID", tableFieldName = "cat_id")
    String catId;

    /**
     * 角色ID
     */
    @AnnoField(title = "商品ID", tableFieldName = "product_id")
    String productId;
}
