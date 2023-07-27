package site.sorghum.anno.modular.type.parser;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import org.noear.solon.annotation.Component;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.Mapping;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.input.Options;
import site.sorghum.anno.common.util.DbContextUtil;
import site.sorghum.anno.metadata.AnField;
import site.sorghum.anno.modular.type.TypeParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类型解析器
 *
 * @author Sorghum
 * @since 2023/07/06
 */
@Component
public class OptionsTypeParser implements TypeParser {

    @SneakyThrows
    @Override
    public Map<String, Object> parseDisplay(AmisBase amisBase, AnField anField) {
        Mapping mappingItem = new Mapping();
        HashMap<String, Object> mapping = new HashMap<>();
        String optionTypeSql = anField.getOptionTypeSql();
        if (StrUtil.isNotBlank(optionTypeSql)) {
            List<Map<String, Object>> mapList = DbContextUtil.dbContext().sql(optionTypeSql).getDataList().getMapList();
            for (Map<String, Object> map : mapList) {
                mapping.put(map.get("value").toString(), map.get("label"));
            }
        } else {
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
        Options options = new Options();
        BeanUtil.copyProperties(formItem, options);
        List<Options.Option> optionItemList = new ArrayList<>();
        String optionTypeSql = anField.getOptionTypeSql();
        if (StrUtil.isNotBlank(optionTypeSql)) {
            List<Map<String, Object>> mapList = DbContextUtil.dbContext().sql(optionTypeSql).getDataList().getMapList();
            for (Map<String, Object> map : mapList) {
                Options.Option optionItem = new Options.Option();
                optionItem.setLabel(MapUtil.getStr(map, "label"));
                optionItem.setValue(map.get("value"));
                optionItemList.add(optionItem);
            }
        } else {
            for (AnField.OptionData optionData : anField.getOptionDatas()) {
                Options.Option optionItem = new Options.Option();
                optionItem.setLabel(optionData.getLabel());
                optionItem.setValue(optionData.getValue());
                optionItemList.add(optionItem);
            }
        }
        options.setOptions(optionItemList);
        return options;
    }
}
