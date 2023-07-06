package site.sorghum.anno.modular.type;

import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.util.JSONUtil;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 类型解析器
 *
 * @author Sorghum
 * @since 2023/07/06
 */
public interface TypeParser {

    Map<String,Object> parseDisplay(FormItem formItem, AnnoField annoField);

    FormItem parseEdit(FormItem formItem,AnnoField annoField);

    default Map<String,Object> mergeObj(Object obj1, Object obj2){
        HashMap<String, Object> map1 = JSONUtil.parseObject(obj1, HashMap.class);
        HashMap<String, Object> map2 = JSONUtil.parseObject(obj2, HashMap.class);
        map2.putAll(map1);
        return map2;
    }
}
