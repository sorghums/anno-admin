package site.sorghum.anno.modular.type.parser;

import org.noear.solon.annotation.Component;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno.metadata.AnField;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.type.TypeParser;
import site.sorghum.anno.common.util.JSONUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认类型解析器
 *
 * @author Sorghum
 * @since 2023/07/07
 */
@Component
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
