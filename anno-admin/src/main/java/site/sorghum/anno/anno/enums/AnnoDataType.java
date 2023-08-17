package site.sorghum.anno.anno.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno.amis.type.TypeParserFactory;

import java.util.Map;

/**
 * Anno数据类型
 *
 * @author sorghum
 * @since 2023/05/27
 */
@AllArgsConstructor
@Getter
public enum AnnoDataType {
    STRING("input-text", "字符串","text"),
    FILE("input-file", "文件","link"),
    IMAGE("input-image", "图片","static-image"),
    NUMBER("input-number", "数字","text"),
    DATE("input-date", "日期","text"),
    DATETIME("input-datetime", "日期时间","text"),
    OPTIONS("select", "下拉框","text"),
    PICKER("picker", "下拉框[弹出]","text"),
    TREE("tree-select", "树形下拉框","input-tree"),
    RICH_TEXT("input-rich-text", "富文本","text"),
    CODE_EDITOR("editor","代码编辑器","tpl")
    ;

    /**
     * 代码
     */
    private final String code;

    private final String name;

    private final String showCode;


    @SneakyThrows
    public static FormItem editorExtraInfo(FormItem item, AnField anField) {
        AnnoDataType annoDataType = anField.getDataType();
        item.setType(annoDataType.getCode());
        return TypeParserFactory.getTypeParser(annoDataType).parseEdit(item,anField);
    }

    @SneakyThrows
    public static Map<String,Object> displayExtraInfo(AmisBase item, AnField anField) {
        AnnoDataType annoDataType = anField.getDataType();
        item.setType(annoDataType.getShowCode());
        return TypeParserFactory.getTypeParser(annoDataType).parseDisplay(item,anField);
    }

}
