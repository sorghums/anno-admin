package site.sorghum.anno.modular.type;

import org.noear.solon.annotation.Component;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.util.JSONUtil;

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
    public Map<String, Object> parseDisplay(AmisBase amisBase, AnnoField annoField) {
        return JSONUtil.parseObject(amisBase, HashMap.class);
    }

    @Override
    public FormItem parseEdit(FormItem formItem, AnnoField annoField) {
        return formItem;
    }
}
