package site.sorghum.anno.amis.type.parser;

import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno.amis.type.TypeParser;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认类型解析器
 *
 * @author Sorghum
 * @since 2023/07/07
 */
@Named
public class DefaultTypeParser implements TypeParser {
    @Override
    public Map<String, Object> parseDisplay(AmisBase amisBase, AnField anField) {
        return JSONUtil.toBean(amisBase, HashMap.class);
    }

    @Override
    public FormItem parseEdit(FormItem formItem, AnField anField) {
        return formItem;
    }
}
