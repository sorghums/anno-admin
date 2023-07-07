package site.sorghum.anno.modular.anno.enums;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.Image;
import site.sorghum.amis.entity.display.Mapping;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.input.InputDatetime;
import site.sorghum.amis.entity.input.InputTree;
import site.sorghum.amis.entity.input.Options;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoImageType;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.modular.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import site.sorghum.anno.modular.type.TypeParserFactory;
import site.sorghum.anno.util.DbContextUtil;
import site.sorghum.anno.util.JSONUtil;

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
    TREE("tree-select", "树形下拉框","input-tree"),
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

    private static Map<String,Object> mergeObj(Object obj1, Object obj2){
        HashMap<String, Object> map1 = JSONUtil.parseObject(obj1, HashMap.class);
        HashMap<String, Object> map2 = JSONUtil.parseObject(obj2, HashMap.class);
        map2.putAll(map1);
        return map2;
    }

    private static Map<String,Object> mergeObj(Object obj1){
        return JSONUtil.parseObject(obj1, HashMap.class);
    }
}
