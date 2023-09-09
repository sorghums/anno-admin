package site.sorghum.anno.amis.type.parser;

import cn.hutool.core.bean.BeanUtil;
import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.Mapping;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.input.Options;
import site.sorghum.amis.entity.input.Picker;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno.amis.type.TypeParser;
import site.sorghum.anno.amis.util.AmisCommonUtil;

import java.util.List;
import java.util.Map;

/**
 * 类型解析器
 *
 * @author Sorghum
 * @since 2023/07/06
 */
@Named
public class OptionsTypeParser implements TypeParser {

    @Override
    public Map<String, Object> parseDisplay(AmisBase amisBase, AnField anField) {
        Mapping mappingItem = new Mapping();
        Map<Object, Object> mapping = AmisCommonUtil.getOptionData(anField);
        mappingItem.setMap(mapping);
        return mergeObj(mappingItem, amisBase);
    }

    @Override
    public FormItem parseEdit(FormItem formItem, AnField anField) {
        Picker picker = new Picker();
        BeanUtil.copyProperties(formItem, picker);
        List<Options.Option> optionItemList = AmisCommonUtil.getOptions(anField);
        picker.setOptions(optionItemList);
        return picker;
    }
}
