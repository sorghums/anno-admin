package site.sorghum.anno.modular.type;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import org.noear.solon.annotation.Component;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.Mapping;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.input.Options;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.util.DbContextUtil;

import java.sql.SQLException;
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
    public Map<String, Object> parseDisplay(AmisBase amisBase, AnnoField annoField) {
        Mapping mappingItem = new Mapping();
        HashMap<String, Object> mapping = new HashMap<>();
        AnnoOptionType annoOptionType = annoField.optionType();
        if (StrUtil.isNotBlank(annoOptionType.sql())) {
            List<Map<String, Object>> mapList = DbContextUtil.dbContext().sql(annoOptionType.sql()).getDataList().getMapList();
            for (Map<String, Object> map : mapList) {
                mapping.put(map.get("value").toString(), map.get("label"));
            }
        } else {
            for (AnnoOptionType.OptionData optionData : annoOptionType.value()) {
                mapping.put(optionData.value(), optionData.label());
            }
        }
        mappingItem.setMap(mapping);
        return mergeObj(mappingItem, amisBase);
    }

    @SneakyThrows
    @Override
    public FormItem parseEdit(FormItem formItem, AnnoField annoField) {
        Options options = new Options();
        BeanUtil.copyProperties(formItem, options);
        List<Options.Option> optionItemList = new ArrayList<>();
        AnnoOptionType annoOptionType = annoField.optionType();
        if (StrUtil.isNotBlank(annoOptionType.sql())) {
            List<Map<String, Object>> mapList = DbContextUtil.dbContext().sql(annoOptionType.sql()).getDataList().getMapList();
            for (Map<String, Object> map : mapList) {
                Options.Option optionItem = new Options.Option();
                optionItem.setLabel(MapUtil.getStr(map, "label"));
                optionItem.setValue(map.get("value"));
                optionItemList.add(optionItem);
            }
        } else {
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
}
