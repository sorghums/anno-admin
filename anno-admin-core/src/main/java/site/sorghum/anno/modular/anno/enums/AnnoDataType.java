package site.sorghum.anno.modular.anno.enums;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoImageType;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.util.DbContextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    RICH_TEXT("input-rich-text", "富文本","text"),
    EDITOR("input-editor", "编辑器","text"),
    ;

    /**
     * 代码
     */
    private final String code;

    private final String name;

    private final String showCode;


    @SneakyThrows
    public static void editorExtraInfo(Map<String ,Object> item, AnnoField annoField) {
        AnnoDataType annoDataType = annoField.dataType();
        item.put("type",annoDataType.getCode());
        if (annoDataType.equals(OPTIONS)) {
            List<JSONObject> options = new ArrayList<>();
            AnnoOptionType annoOptionType = annoField.optionType();
            if (StrUtil.isNotBlank(annoOptionType.sql())){
                List<Map<String, Object>> mapList = DbContextUtil.dbContext().sql(annoOptionType.sql()).getDataList().getMapList();
                for (Map<String, Object> map : mapList) {
                    JSONObject option = new JSONObject();
                    option.put("label", map.get("label"));
                    option.put("value", map.get("value"));
                    options.add(option);
                }
            }else {
                for (AnnoOptionType.OptionData optionData : annoOptionType.value()) {
                    JSONObject option = new JSONObject();
                    option.put("label", optionData.label());
                    option.put("value", optionData.value());
                    options.add(option);
                }
            }
            item.put("options",options);
            return;
        }
        if (annoDataType.equals(DATETIME)){
            item.put("format","YYYY-MM-DD HH:mm:ss");
            return;
        }

    }

    @SneakyThrows
    public static void displayExtraInfo(JSONObject item, AnnoField annoField) {
        AnnoDataType annoDataType = annoField.dataType();
        item.put("type",annoDataType.getShowCode());
        item.put("placeholder","无");
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
        if (annoDataType.equals(OPTIONS)){
            item.put("type","mapping");
            HashMap<String, String> mapping = new HashMap<>();
            AnnoOptionType annoOptionType = annoField.optionType();
            if (StrUtil.isNotBlank(annoOptionType.sql())){
                List<Map<String, Object>> mapList = DbContextUtil.dbContext().sql(annoOptionType.sql()).getDataList().getMapList();
                for (Map<String, Object> map : mapList) {
                    mapping.put(map.get("value").toString(),map.get("label").toString());
                }
            }else {
                for (AnnoOptionType.OptionData optionData : annoOptionType.value()) {
                    JSONObject option = new JSONObject();
                    mapping.put(optionData.value(),optionData.label());
                }
            }
            item.put("map",mapping);
        }
    }

}
