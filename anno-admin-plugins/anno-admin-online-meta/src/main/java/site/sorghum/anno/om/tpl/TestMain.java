package site.sorghum.anno.om.tpl;

import lombok.Data;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.*;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.plugin.join.aop.JoinResMap;

import java.util.HashMap;
import java.util.Map;

@Data
@AnnoMain(
    name = "测试",
    tableName = "test_main",
    annoPermission = @AnnoPermission(baseCode = "test_main", baseCodeTranslate = "测试"),
    canRemove = true,
    autoMaintainTable = true
)
public class TestMain {

    @AnnoField(
        tableFieldName = "test_column",
        fieldSize = 256,
        show = true,
        pkField = false,
            edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
            ),
            search = @AnnoSearch(
            notNull = false,
            defaultValue = ""
        ),
        dataType = AnnoDataType.OPTIONS,
            optionType = @AnnoOptionType(
                value = {
                    @AnnoOptionType.OptionData(value = "1", label = "测试1"),
                },
                optionAnno = @AnnoOptionType.OptionAnnoClass(annoClass = site.sorghum.anno.om.ao.OnlineMeta.class)
            ),
                imageType = @AnnoImageType(
                enlargeAble = true,
                width = 50,
                height = 50
            ),
            codeType = @AnnoCodeType(
                mode = "text"
            ),
            treeType = @AnnoTreeType(
                value = {
                    @AnnoTreeType.TreeData(id = "1", label = "测试1", pid = "0"),
                },
                treeAnno = @AnnoTreeType.TreeAnnoClass(annoClass = site.sorghum.anno.om.ao.OnlineMeta.class)
            ),
                fileType = @AnnoFileType(
                fileType = "*",
                fileMaxCount = 1,
                fileMaxSize = 5
            ),
        title = "测试列"
    )
    String testColumn;


    @JoinResMap
    Map<String, Object> joinResMap = new HashMap<>();
}