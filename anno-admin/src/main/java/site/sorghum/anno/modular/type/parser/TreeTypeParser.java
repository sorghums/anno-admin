package site.sorghum.anno.modular.type.parser;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Named;
import lombok.SneakyThrows;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.Mapping;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.input.InputTree;
import site.sorghum.amis.entity.input.Options;
import site.sorghum.anno.metadata.AnField;
import site.sorghum.anno.modular.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import site.sorghum.anno.modular.type.TypeParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sorghum
 * @since 2023/07/07
 */
@Named
public class TreeTypeParser implements TypeParser {

    @Db
    DbContext dbContext;

    @SneakyThrows
    @Override
    public Map<String, Object> parseDisplay(AmisBase amisBase, AnField anField) {
        Mapping mappingItem = new Mapping();
        HashMap<String, Object> mapping = new HashMap<>();
        String treeTypeSql = anField.getTreeTypeSql();
        if (StrUtil.isNotBlank(treeTypeSql)) {
            List<Map<String, Object>> mapList = dbContext.sql(treeTypeSql).getDataList().getMapList();
            for (Map<String, Object> map : mapList) {
                mapping.put(MapUtil.getStr(map, "id"), map.get("label"));
            }
        } else {
            for (AnField.TreeData treeData : anField.getTreeDatas()) {
                mapping.put(treeData.getValue(), treeData.getLabel());
            }
        }
        mappingItem.setMap(mapping);
        return mergeObj(mappingItem, amisBase);
    }

    @SneakyThrows
    @Override
    public FormItem parseEdit(FormItem formItem, AnField anField) {
        InputTree inputTree = new InputTree();
        BeanUtil.copyProperties(formItem, inputTree);
        List<Options.Option> optionItemList = new ArrayList<>();
        String treeTypeSql = anField.getTreeTypeSql();
        if (StrUtil.isNotBlank(treeTypeSql)) {
            List<Map<String, Object>> mapList = dbContext.sql(treeTypeSql).getDataList().getMapList();
            List<AnnoTreeDTO<String>> trees = AnnoUtil.buildAnnoTree(
                mapList, "label", "id", "pid"
            );
            optionItemList = AnnoTreeDTO.toOptions(trees);
        } else {
            List<AnnoTreeDTO<String>> trees = new ArrayList<>();
            for (AnField.TreeData treeData : anField.getTreeDatas()) {
                AnnoTreeDTO<String> tree = new AnnoTreeDTO<>();
                tree.setId(treeData.getId());
                tree.setLabel(treeData.getLabel());
                tree.setValue(treeData.getValue());
                tree.setParentId(treeData.getPid());
                trees.add(tree);
            }
            optionItemList = AnnoTreeDTO.toOptions(trees);
        }
        inputTree.setOptions(optionItemList);
        return inputTree;
    }

}
