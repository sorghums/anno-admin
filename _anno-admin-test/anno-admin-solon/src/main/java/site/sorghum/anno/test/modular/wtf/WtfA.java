package site.sorghum.anno.test.modular.wtf;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoLeftTree;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoTableButton;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.pre.plugin.ao.SysUser;
import site.sorghum.anno.pre.suppose.model.BaseMetaModel;
import site.sorghum.anno.test.modular.ebusiness.BusinessProductCat;
import site.sorghum.anno.test.modular.ebusiness.BusinessVirtualProduct;
import site.sorghum.anno.test.modular.ebusiness.BusinessVirtualTable;

/**
 * 电商商品
 *
 * @author Sorghum
 * @since 2023/07/04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "沃特发表A")
@Table("wtf_a")
public class WtfA extends BaseMetaModel {

    @AnnoField(
        title = "名称",
        tableFieldName = "name",
        search = @AnnoSearch,
        edit = @AnnoEdit)
    String name;

    @AnnoField(
        title = "年龄",
        tableFieldName = "age",
        edit = @AnnoEdit)
    String age;

    @AnnoButton(name = "沃特发B",
        o2mJoinButton = @AnnoButton.O2MJoinButton(joinAnnoMainClazz = WtfB.class,
            joinThisClazzField = "id",
            joinOtherClazzField = "wtfA"))
    private Object wtfB;

}
