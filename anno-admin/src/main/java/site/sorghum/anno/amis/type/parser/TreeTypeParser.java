package site.sorghum.anno.amis.type.parser;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.SneakyThrows;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.Mapping;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.input.InputTree;
import site.sorghum.amis.entity.input.Options;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.amis.type.TypeParser;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.anno.proxy.DbServiceWithProxy;
import site.sorghum.anno.anno.util.AnnoUtil;

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

    @Inject
    DbServiceWithProxy dbServiceWithProxy;

    @Inject
    MetadataManager metadataManager;


    @SneakyThrows
    @Override
    public Map<String, Object> parseDisplay(AmisBase amisBase, AnField anField) {
        Mapping mappingItem = new Mapping();
        HashMap<Object, Object> mapping = new HashMap<>();
        String treeTypeSql = anField.getTreeTypeSql();
        AnField.TreeAnnoClass treeOptionAnnoClass = anField.getTreeOptionAnnoClass();
        if (StrUtil.isNotBlank(treeTypeSql)) {
            List<Map<String, Object>> mapList = dbContext.sql(treeTypeSql).getDataList().getMapList();
            for (Map<String, Object> map : mapList) {
                mapping.put(MapUtil.getStr(map, "id"), map.get("label"));
            }
        } else if (!treeOptionAnnoClass.getAnnoClass().equals(Object.class)){
            AnEntity anEntity = metadataManager.getEntity(treeOptionAnnoClass.getAnnoClass());
            List<?> dataList = dbServiceWithProxy.list(
                metadataManager.getTableParam(anEntity.getClazz()),
                new ArrayList<>()
            );
            for (Object data : dataList) {
                mapping.put(
                    ReflectUtil.getFieldValue(data, treeOptionAnnoClass.getIdKey()),
                    ReflectUtil.getFieldValue(data, treeOptionAnnoClass.getLabelKey())
                );
            }
        } else if (CollUtil.isNotEmpty(anField.getTreeDatas())){
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
        AnField.TreeAnnoClass treeOptionAnnoClass = anField.getTreeOptionAnnoClass();

        if (StrUtil.isNotBlank(treeTypeSql)) {
            List<Map<String, Object>> mapList = dbContext.sql(treeTypeSql).getDataList().getMapList();
            List<AnnoTreeDTO<String>> trees = AnnoUtil.buildAnnoTree(
                mapList, "label", "id", "pid"
            );
            optionItemList = AnnoTreeDTO.toOptions(trees);
        } else if(!treeOptionAnnoClass.getAnnoClass().equals(Object.class)){
            AnEntity anEntity = metadataManager.getEntity(treeOptionAnnoClass.getAnnoClass());
            List<?> dataList = dbServiceWithProxy.list(
                metadataManager.getTableParam(anEntity.getClazz()),
                new ArrayList<>()
            );
            List<AnnoTreeDTO<String>> trees = AnnoUtil.buildAnnoTree(
                dataList, treeOptionAnnoClass.getLabelKey(), treeOptionAnnoClass.getIdKey(), treeOptionAnnoClass.getPidKey()
            );
            optionItemList = AnnoTreeDTO.toOptions(trees);
        } else if (CollUtil.isNotEmpty(anField.getTreeDatas())){
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
