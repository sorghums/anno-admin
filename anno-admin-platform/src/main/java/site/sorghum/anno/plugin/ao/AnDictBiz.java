package site.sorghum.anno.plugin.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoOrder;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.anno.enums.AnnoDataType;

@AnnoMain(
    name = "业务字典",
    tableName = "an_dict_biz",
    annoPermission = @AnnoPermission(
        baseCode = "an_dict_biz",
        baseCodeTranslate = "业务字典"
    ),
    annoOrder = {
        @AnnoOrder(orderValue = "id", orderType = "desc")
    },
    annoTree = @AnnoTree(
        label = "name",
        parentKey = "pid",
        key = "id",
        displayAsTree = true
    )
)
@Data
@EqualsAndHashCode(callSuper = true)
public class AnDictBiz extends AnDictBase {

    @AnnoField(
        title = "父字典",
        dataType = AnnoDataType.TREE,
        treeType = @AnnoTreeType(
            treeAnno = @AnnoTreeType.TreeAnnoClass(annoClass = AnDictBiz.class)
        ),
        fieldSize = 256,
        edit = @AnnoEdit
    )
    @Override
    public String getPid() {
        return super.getPid();
    }
}
