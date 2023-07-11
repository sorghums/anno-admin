package site.sorghum.anno.modular.anno.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.type.TypeParserFactory;

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
    FILE("input-file", "文件","text"),
    IMAGE("input-image", "图片","static-image"),
    NUMBER("input-number", "数字","text"),
    DATE("input-date", "日期","text"),
    DATETIME("input-datetime", "日期时间","text"),
    OPTIONS("select", "下拉框","text"),
    TREE("tree-select", "树形下拉框","input-tree"),
    RICH_TEXT("input-rich-text", "富文本","text"),
    CODE_EDITOR("editor","代码编辑器","code"),
    ;

    /**
     * 代码
     */
    private final String code;

    private final String name;

    private final String showCode;


    @SneakyThrows
    public static FormItem editorExtraInfo(FormItem item, AnnoField annoField) {
        AnnoDataType annoDataType = annoField.dataType();
        item.setType(annoDataType.getCode());
        return TypeParserFactory.getTypeParser(annoDataType).parseEdit(item,annoField);
    }

    @SneakyThrows
    public static Map<String,Object> displayExtraInfo(AmisBase item, AnnoField annoField) {
        AnnoDataType annoDataType = annoField.dataType();
        item.setType(annoDataType.getShowCode());
        return TypeParserFactory.getTypeParser(annoDataType).parseDisplay(item,annoField);
    }

}
