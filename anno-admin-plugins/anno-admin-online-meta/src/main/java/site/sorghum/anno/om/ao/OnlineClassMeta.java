package site.sorghum.anno.om.ao;

import lombok.Data;
import org.noear.wood.annotation.PrimaryKey;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.type.AnnoCodeType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.proxy.field.SnowIdSupplier;
import site.sorghum.anno.om.javacmd.PreviewClassMetaCmd;
import site.sorghum.plugin.join.aop.JoinResMap;

import java.util.HashMap;
import java.util.Map;

/**
 * 在线Class加载器
 *
 * @author Sorghum
 * @since 2024/02/26
 */
@Data
@AnnoMain(name = "在线Class加载器", tableName = "an_class_meta_ao")
public class OnlineClassMeta {

    @AnnoField(title = "主键", tableFieldName = "id",
        show = false,
        fieldSize = 32,
        insertWhenNullSet = SnowIdSupplier.class)
    @PrimaryKey
    String id;


    @AnnoField(title = "完整类名",
        fieldSize = 256,
        tableFieldName = "whole_class_name",
        edit = @AnnoEdit())
    String wholeClassName;


    @AnnoField(title = "元数据",
        tableFieldName = "class_content",
        dataType = AnnoDataType.CODE_EDITOR,
        fieldSize = 256,
        codeType = @AnnoCodeType(mode = "text/x-java"),
        edit = @AnnoEdit())
    String classContent;

    @AnnoButton(
        name = "预览",
        javaCmd = @AnnoButton.JavaCmd(enable = true,runSupplier = PreviewClassMetaCmd.class)
    )
    Object preview;


    @JoinResMap
    Map<String, Object> joinResMap = new HashMap<>();
}
