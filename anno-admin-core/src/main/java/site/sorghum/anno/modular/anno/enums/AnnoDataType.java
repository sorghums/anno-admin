package site.sorghum.anno.modular.anno.enums;

import com.alibaba.fastjson2.JSONObject;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoImageType;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoOptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
    RICH_TEXT("input-rich-text", "富文本","text"),
    EDITOR("input-editor", "编辑器","text"),
    ;

    /**
     * 代码
     */
    private final String code;

    private final String name;

    private final String showCode;


    public static void editorExtraInfo(JSONObject item, AnnoField annoField) {
        AnnoDataType annoDataType = annoField.dataType();
        item.put("type",annoDataType.getCode());
        if (annoDataType.equals(OPTIONS)) {
            List<JSONObject> options = new ArrayList<>();
            AnnoOptionType annoOptionType = annoField.optionType();
            for (AnnoOptionType.OptionData optionData : annoOptionType.value()) {
                JSONObject option = new JSONObject();
                option.put("label", optionData.label());
                option.put("value", optionData.value());
                options.add(option);
            }
            item.put("options",options);
            return;
        }
        if (annoDataType.equals(DATETIME)){
            item.put("format","YYYY-MM-DD HH:mm:ss");
            return;
        }

    }

    public static void displayExtraInfo(JSONObject item, AnnoField annoField) {
        AnnoDataType annoDataType = annoField.dataType();
        item.put("type",annoDataType.getShowCode());
        if (annoDataType.equals(IMAGE)){
            AnnoImageType imageType = annoField.imageType();
            if (imageType.height() > 0 && imageType.width() > 0){
                item.put("height",imageType.height());
                item.put("width",imageType.width());
            }
            item.put("enlargeAble",imageType.enlargeAble());
            item.put("thumbMode",imageType.thumbMode().getMode());
            item.put("thumbRatio",imageType.thumbRatio().getRatio());
            return;
        }
    }

}
