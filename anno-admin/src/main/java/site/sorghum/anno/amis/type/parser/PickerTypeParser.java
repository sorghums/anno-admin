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
import site.sorghum.amis.entity.input.Options;
import site.sorghum.amis.entity.input.Picker;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.amis.type.TypeParser;
import site.sorghum.anno.anno.proxy.DbServiceWithProxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Picker类型解析器
 *
 * @author Sorghum
 * @since 2023/07/06
 */
@Named
public class PickerTypeParser implements TypeParser {

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
        String optionTypeSql = anField.getOptionTypeSql();
        AnField.OptionAnnoClass optionAnnoClass = anField.getOptionAnnoClass();
        if (StrUtil.isNotBlank(optionTypeSql)) {
            List<Map<String, Object>> mapList = dbContext.sql(optionTypeSql).getDataList().getMapList();
            for (Map<String, Object> map : mapList) {
                mapping.put(map.get("value").toString(), map.get("label"));
            }
        } else if(!optionAnnoClass.getAnnoClass().equals(Object.class)){
            AnEntity anEntity = metadataManager.getEntity(optionAnnoClass.getAnnoClass());
            List<?> dataList = dbServiceWithProxy.list(
                anEntity.getClazz(),
                new ArrayList<>()
            );
            for (Object data : dataList) {
                mapping.put(
                    ReflectUtil.getFieldValue(data, optionAnnoClass.getIdKey()),
                    ReflectUtil.getFieldValue(data, optionAnnoClass.getLabelKey())
                );
            }
        } else if (CollUtil.isNotEmpty(anField.getOptionDatas())){
            for (AnField.OptionData optionData : anField.getOptionDatas()) {
                mapping.put(optionData.getValue(), optionData.getLabel());
            }
        }
        mappingItem.setMap(mapping);
        return mergeObj(mappingItem, amisBase);
    }

    @SneakyThrows
    @Override
    public FormItem parseEdit(FormItem formItem, AnField anField) {
        Picker picker = new Picker();
        BeanUtil.copyProperties(formItem, picker);
        List<Options.Option> optionItemList = new ArrayList<>();
        String optionTypeSql = anField.getOptionTypeSql();
        AnField.OptionAnnoClass optionAnnoClass = anField.getOptionAnnoClass();
        if (StrUtil.isNotBlank(optionTypeSql)) {
            List<Map<String, Object>> mapList = dbContext.sql(optionTypeSql).getDataList().getMapList();
            for (Map<String, Object> map : mapList) {
                Options.Option optionItem = new Options.Option();
                optionItem.setLabel(MapUtil.getStr(map, "label"));
                optionItem.setValue(map.get("value"));
                optionItemList.add(optionItem);
            }
        } else if(!optionAnnoClass.getAnnoClass().equals(Object.class)){
            AnEntity anEntity = metadataManager.getEntity(optionAnnoClass.getAnnoClass());
            List<?> dataList = dbServiceWithProxy.list(
                anEntity.getClazz(),
                new ArrayList<>()
            );
            for (Object data : dataList) {
                Options.Option optionItem = new Options.Option();
                optionItem.setLabel(StrUtil.toString(ReflectUtil.getFieldValue(data, optionAnnoClass.getLabelKey())));
                optionItem.setValue(StrUtil.toString(ReflectUtil.getFieldValue(data, optionAnnoClass.getIdKey())));
                optionItemList.add(optionItem);
            }
        } else if (CollUtil.isNotEmpty(anField.getOptionDatas())){
            for (AnField.OptionData optionData : anField.getOptionDatas()) {
                Options.Option optionItem = new Options.Option();
                optionItem.setLabel(optionData.getLabel());
                optionItem.setValue(optionData.getValue());
                optionItemList.add(optionItem);
            }
        }
        picker.setOptions(optionItemList);
        return picker;
    }
}
