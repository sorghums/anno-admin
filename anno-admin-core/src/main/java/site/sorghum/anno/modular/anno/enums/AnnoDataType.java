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
        if (annoDataType.equals(OPTIONS)) {
            Options options = new Options();
            BeanUtil.copyProperties(item,options);
            List<Options.Option> optionItemList = new ArrayList<>();
            AnnoOptionType annoOptionType = annoField.optionType();
            if (StrUtil.isNotBlank(annoOptionType.sql())){
                List<Map<String, Object>> mapList = DbContextUtil.dbContext().sql(annoOptionType.sql()).getDataList().getMapList();
                for (Map<String, Object> map : mapList) {
                    Options.Option optionItem = new Options.Option();
                    optionItem.setLabel(MapUtil.getStr(map,"label"));
                    optionItem.setValue(map.get("value"));
                    optionItemList.add(optionItem);
                }
            }else {
                for (AnnoOptionType.OptionData optionData : annoOptionType.value()) {
                    Options.Option optionItem = new Options.Option();
                    optionItem.setLabel(optionData.label());
                    optionItem.setValue(optionData.value());
                    optionItemList.add(optionItem);
                }
            }
            options.setOptions(optionItemList);
            return options;
        }
        if (annoDataType.equals(DATETIME)){
            InputDatetime inputDatetime = new InputDatetime();
            BeanUtil.copyProperties(item,inputDatetime);
            inputDatetime.setFormat("YYYY-MM-DD HH:mm:ss");
            return inputDatetime;
        }
        if (annoDataType.equals(TREE)) {
            InputTree inputTree = new InputTree();
            BeanUtil.copyProperties(item,inputTree);
            List<Options.Option> optionItemList = new ArrayList<>();
            AnnoTreeType annoTreeType = annoField.treeType();
            if (StrUtil.isNotBlank(annoTreeType.sql())){
                List<Map<String, Object>> mapList = DbContextUtil.dbContext().sql(annoTreeType.sql()).getDataList().getMapList();
                List<AnnoTreeDTO<String>> trees = AnnoUtil.buildAnnoTree(
                        mapList, "label", "id", "pid"
                );
                optionItemList = AnnoTreeDTO.toOptions(trees);
            }else {
                List<AnnoTreeDTO<String>> trees = new ArrayList<>();
                for (AnnoTreeType.TreeData treeData : annoTreeType.value()) {
                    AnnoTreeDTO<String> tree = new AnnoTreeDTO<>();
                    tree.setLabel(treeData.label());
                    tree.setValue(treeData.value());
                    tree.setParentId(treeData.pid());
                    trees.add(tree);
                }
                optionItemList = AnnoTreeDTO.toOptions(trees);
            }
            inputTree.setOptions(optionItemList);
            return inputTree;
        }
        return item;

    }

    @SneakyThrows
    public static Map<String,Object> displayExtraInfo(AmisBase item, AnnoField annoField) {
        AnnoDataType annoDataType = annoField.dataType();
        item.setType(annoDataType.getShowCode());
        if (annoDataType.equals(IMAGE)){
            Image image = new Image();
            AnnoImageType imageType = annoField.imageType();
            if (imageType.height() > 0 && imageType.width() > 0){
                image.setHeight(imageType.height());
                image.setWidth(imageType.width());
            }
            image.setEnlargeAble(imageType.enlargeAble());
            image.setThumbMode(imageType.thumbMode().getMode());
            image.setThumbRatio(imageType.thumbRatio().getRatio());
            return mergeObj(image,item);
        }
        if (annoDataType.equals(OPTIONS)){
            Mapping mappingItem = new Mapping();
            HashMap<String, Object> mapping = new HashMap<>();
            AnnoOptionType annoOptionType = annoField.optionType();
            if (StrUtil.isNotBlank(annoOptionType.sql())){
                List<Map<String, Object>> mapList = DbContextUtil.dbContext().sql(annoOptionType.sql()).getDataList().getMapList();
                for (Map<String, Object> map : mapList) {
                    mapping.put(map.get("value").toString(),map.get("label"));
                }
            }else {
                for (AnnoOptionType.OptionData optionData : annoOptionType.value()) {
                    mapping.put(optionData.value(),optionData.label());
                }
            }
            mappingItem.setMap(mapping);
            return mergeObj(mappingItem,item);
        }
        if (annoDataType.equals(TREE)){
            Mapping mappingItem = new Mapping();
            HashMap<String, Object> mapping = new HashMap<>();
            AnnoTreeType annoTreeType = annoField.treeType();
            if (StrUtil.isNotBlank(annoTreeType.sql())){
                List<Map<String, Object>> mapList = DbContextUtil.dbContext().sql(annoTreeType.sql()).getDataList().getMapList();
                for (Map<String, Object> map : mapList) {
                    mapping.put(map.get("value").toString(),map.get("label"));
                }
            }else {
                for (AnnoTreeType.TreeData treeData : annoTreeType.value()) {
                    mapping.put(treeData.value(),treeData.label());
                }
            }
            mappingItem.setMap(mapping);
            return mergeObj(mappingItem,item);
        }
        return mergeObj(item);
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
