package site.sorghum.anno.modular.type.parser;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import org.noear.solon.annotation.Component;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.Image;
import site.sorghum.amis.entity.display.Mapping;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.input.InputTree;
import site.sorghum.amis.entity.input.Options;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoImageType;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.modular.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import site.sorghum.anno.modular.type.TypeParser;
import site.sorghum.anno.util.DbContextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sorghum
 * @since 2023/07/07
 */
@Component
public class TreeTypeParser implements TypeParser {

    @SneakyThrows
    @Override
    public Map<String, Object> parseDisplay(AmisBase amisBase, AnnoField annoField) {
        Mapping mappingItem = new Mapping();
        HashMap<String, Object> mapping = new HashMap<>();
        AnnoTreeType annoTreeType = annoField.treeType();
        if (StrUtil.isNotBlank(annoTreeType.sql())){
            List<Map<String, Object>> mapList = DbContextUtil.dbContext().sql(annoTreeType.sql()).getDataList().getMapList();
            for (Map<String, Object> map : mapList) {
                mapping.put(MapUtil.getStr(map,"id"),map.get("label"));
            }
        }else {
            for (AnnoTreeType.TreeData treeData : annoTreeType.value()) {
                mapping.put(treeData.value(),treeData.label());
            }
        }
        mappingItem.setMap(mapping);
        return mergeObj(mappingItem,amisBase);
    }

    @SneakyThrows
    @Override
    public FormItem parseEdit(FormItem formItem, AnnoField annoField) {
        InputTree inputTree = new InputTree();
        BeanUtil.copyProperties(formItem,inputTree);
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

}
