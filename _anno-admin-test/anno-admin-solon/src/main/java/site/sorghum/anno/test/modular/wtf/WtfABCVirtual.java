package site.sorghum.anno.test.modular.wtf;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoProxy;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.pre.suppose.model.BaseMetaModel;

/**
 * 电商商品
 *
 * @author Sorghum
 * @since 2023/07/04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "沃特发表ABC",virtualTable = true,annoProxy = @AnnoProxy(value = WtfABCVirtualProxy.class))
public class WtfABCVirtual extends BaseMetaModel {

    @AnnoField(
        title = "名称",
        tableFieldName = "name",
        virtualColumn = true,
        search = @AnnoSearch,
        edit = @AnnoEdit)
    String name;

    @AnnoField(
        title = "年龄",
        tableFieldName = "age",
        virtualColumn = true,
        edit = @AnnoEdit)
    String age;

    @AnnoField(
        title = "性格",
        tableFieldName = "attr",
        virtualColumn = true,
        search = @AnnoSearch,
        edit = @AnnoEdit)
    String attr;

    @AnnoField(
        title = "地址",
        tableFieldName = "location",
        virtualColumn = true,
        search = @AnnoSearch,
        edit = @AnnoEdit)
    String location;
    
}
