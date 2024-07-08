package site.sorghum.anno.plugin.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.PrimaryKey;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.type.AnnoCodeType;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.proxy.field.SnowIdSupplier;
import site.sorghum.anno.plugin.javacmd.ExportJarCmd;
import site.sorghum.anno.plugin.javacmd.PreviewMetaCmd;
import site.sorghum.plugin.join.aop.JoinResMap;

import java.util.HashMap;
import java.util.Map;

/**
 * 元数据加载器
 *
 * @author Sorghum
 * @since 2024/02/26
 */
@Data
@AnnoMain(name = "元数据加载器", tableName = "an_meta_ao")
public class OnlineMeta {

    @AnnoField(title = "主键", tableFieldName = "id",
        show = false,
        fieldSize = 32,
        insertWhenNullSet = SnowIdSupplier.class)
    @PrimaryKey
    String id;


    @AnnoField(title = "元数据",
        tableFieldName = "yml_content",
        dataType = AnnoDataType.CODE_EDITOR,
        codeType = @AnnoCodeType(mode = "yaml"),
        edit = @AnnoEdit())
    String ymlContent;


    @AnnoButton(
        name = "导出Jar包",
        javaCmd = @AnnoButton.JavaCmd(enable = true,runSupplier = ExportJarCmd.class)
    )
    Object exportJar;

    @AnnoButton(
        name = "预览",
        javaCmd = @AnnoButton.JavaCmd(enable = true,runSupplier = PreviewMetaCmd.class)
    )
    Object preview;


    @JoinResMap
    Map<String, Object> joinResMap = new HashMap<>();
}
