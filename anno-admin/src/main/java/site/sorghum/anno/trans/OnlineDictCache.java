package site.sorghum.anno.trans;

import cn.hutool.core.util.StrUtil;
import jakarta.inject.Named;
import lombok.Data;
import site.sorghum.anno.anno.entity.common.TagEnumLabel;

import java.util.*;

@Named
public class OnlineDictCache {

    @Data
    public static class OnlineDict {
        /**
         * ID
         */
        String id;

        /**
         * 父级ID
         */
        String pid;

        /**
         * 父级值
         */
        String parentValue;

        /**
         * 值
         */
        String value;

        /**
         * 名称
         */
        String name;

        /**
         * 颜色
         */
        String color;
    }

    /**
     * 缓存的字典数据，以字典名称作为键，以字典数据作为值
     */
    private final Map<String, List<OnlineDict>> dictMapCache = new HashMap<>();

    /**
     * 将字典数据存入缓存中
     *
     * @param dictName 字典名称
     * @param dictData 字典数据，以键值对形式存储
     */
    public void put(String dictName, List<OnlineDict> dictData) {
        this.dictMapCache.put(dictName, dictData);
    }


    /**
     * 从缓存中获取指定字典名称的字典数据
     *
     * @param dictName 字典名称
     * @return 包含字典数据的Map，若不存在则返回null
     */
    public List<OnlineDict> get(String dictName) {
        return this.dictMapCache.get(dictName);
    }


    /**
     * 从缓存中获取指定字典名称的字典数据
     *
     * @param dictName 字典名称
     * @return 包含字典数据的Map，若不存在则返回null
     */
    public List<OnlineDict> getForLoadDict(String dictName) {
        List<OnlineDict> onlineDictList = get(dictName);
        if (onlineDictList == null){
            return Collections.emptyList();
        }
        onlineDictList.stream().filter(it -> Objects.equals(it.getParentValue(),dictName)).forEach(it -> {it.setParentValue(null);});
        return onlineDictList;
    }

    /**
     * 从缓存中获取指定字典名称的字典数据
     *
     * @param dictName 字典名称
     * @return 包含字典数据的Map，若不存在则返回null
     */
    public Map<String, Object> getValueLabel(String dictName) {
        return get(dictName).stream().collect(HashMap::new, (m, v) -> {
            Object name = v.getName();
            if (StrUtil.isNotBlank(v.getColor()) && !Objects.equals(v.getColor(),"#00000000")){
                name = new TagEnumLabel(name.toString(), v.getColor());
            }
            m.put(v.getValue(), name);
        }, HashMap::putAll);
    }

}
