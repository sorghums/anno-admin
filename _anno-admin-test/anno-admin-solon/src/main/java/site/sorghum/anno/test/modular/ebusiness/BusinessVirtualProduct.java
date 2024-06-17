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
@AnnoMain(name = "商品虚拟商品关联管理")
@Table("business_virtual_product_relation")
public class BusinessVirtualProduct extends BaseMetaModel implements Serializable {

    /**
     * 虚拟ID
     */
    @AnnoField(title = "虚拟ID", tableFieldName = "cat_id")
    String virtualId;

    /**
     * 角色ID
     */
    @AnnoField(title = "商品ID", tableFieldName = "product_id")
    String productId;
}
