package site.sorghum.anno.amis.type;

import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._metadata.AnField;

import java.util.HashMap;
import java.util.Map;

/**
 * 类型解析器
 *
 * @author Sorghum
 * @since 2023/07/06
 */
public interface TypeParser {

    /**
     * 解析显示
     *
     * @param amisBase  基础组件
     * @param anField An字段
     * @return {@link Map}<{@link String},{@link Object}>
     */
    Map<String,Object> parseDisplay(AmisBase amisBase, AnField anField);

    /**
     * 解析编辑
     *
     * @param formItem 表单项
     * @param anField  An字段
     * @return {@link FormItem}
     */
    FormItem parseEdit(FormItem formItem, AnField anField);

    default Map<String,Object> mergeObj(Object obj1, Object obj2){
        HashMap<String, Object> map1 = JSONUtil.toBean(obj1, HashMap.class);
        HashMap<String, Object> map2 = JSONUtil.toBean(obj2, HashMap.class);
        map2.putAll(map1);
        return map2;
    }
}
