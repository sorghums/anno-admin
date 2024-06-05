package site.sorghum.anno.plugin.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.PrimaryKeyModel;

@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(
    name = "平台信息",
    tableName = "an_platform",
    annoPermission = @AnnoPermission(enable = true, baseCode = "an_platform", baseCodeTranslate = "平台信息"),
    canRemove = false
)
public class AnPlatform extends PrimaryKeyModel {

    @AnnoField(title = "平台名称", tableFieldName = "name", edit = @AnnoEdit(addEnable = false))
    String name;

    @AnnoField(title = "平台Logo", tableFieldName = "platform_logo", dataType = AnnoDataType.IMAGE, edit = @AnnoEdit(addEnable = false))
    String platformLogo;

    @AnnoField(title = "平台描述", tableFieldName = "description", edit = @AnnoEdit(addEnable = false))
    String description;
}
