package site.sorghum.anno.modular.anno.util;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.FIFOCache;
import cn.hutool.core.date.StopWatch;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import site.sorghum.anno.modular.amis.model.Amis;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.core.util.ResourceUtil;
import site.sorghum.anno.util.JSONUtil;

import java.net.URL;
import java.util.Map;

/**
 * 模板工具
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Slf4j
public class TemplateUtil {

    private static final FIFOCache<String, Object> FIFO_CACHE = CacheUtil.newFIFOCache(500);

    /**
     * 得到crud模板
     *
     * @param clazz      clazz
     * @param properties 页面参数
     * @return {@link JSONObject}
     */
    public static Map<String, Object> getCrudTemplate(Class<?> clazz, Map<String, Object> properties) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Amis amis = JSONUtil.parseObject(getTemplate("crudTemplate.json"), Amis.class);
        // 添加过滤
        amis.addCrudFilter(clazz);
        // 添加列
        amis.addCrudColumns(clazz);
        // 添加删除信息
        amis.addCrudDeleteButton(clazz);
        // 添加编辑信息
        amis.addCrudEditInfo(clazz);
        if (properties.getOrDefault("isM2m", false).equals(false)) {
            // 添加自定义按钮信息
            amis.addCrudColumnButtonInfo(clazz);
        }
        // 添加新增信息
        amis.addCrudAddInfo(clazz);
        // 添加树边栏
        amis.addCommonTreeAside(clazz, properties);
        // 添加m2m多选框
        if (properties.getOrDefault("isM2m", false).equals(true)) {
            amis.addCrudM2mCheckBox(clazz);
        }
        stopWatch.stop();
        log.debug("crud模板：{}", JSONUtil.toJSONString(amis));
        log.debug("crud模板生成耗时：{}ms", stopWatch.getTotalTimeMillis());
        return amis;
    }

    /**
     * 得到crud模板
     *
     * @param clazz      clazz
     * @param properties 页面参数
     * @return {@link JSONObject}
     */
    public static Map<String, Object> getCrudM2mTemplate(Class<?> clazz, Map<String, Object> properties) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Amis amis = JSONUtil.parseObject(getTemplate("m2mTemplate.json"), Amis.class);
        // 添加过滤
        amis.addCrudFilter(clazz);
        // 添加列
        amis.addCrudColumns(clazz);
        // 添加关联查询的表格信息
        properties.put("isM2m", true);
        amis.addRelationCrudData(clazz, getCrudTemplate(clazz, properties));
        // 添加编辑信息
        amis.addCrudEditInfo(clazz);
        // 添加删除对应关联关系信息的按钮
        amis.addDeleteRelationEditInfo(clazz);
        stopWatch.stop();
        log.debug("crud模板：{}", JSONUtil.toJSONString(amis));
        log.debug("crud模板生成耗时：{}ms", stopWatch.getTotalTimeMillis());
        return amis;
    }

    /**
     * 让树模板
     * 得到crud模板
     *
     * @param clazz      clazz
     * @param properties 页面参数
     * @return {@link Map}<{@link String} ,{@link Object}>
     */
    public static Map<String, Object> getTreeTemplate(Class<?> clazz, Map<String, Object> properties) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Amis amis = JSONUtil.parseObject(getTemplate("treeTemplate.json"), Amis.class);
        // 添加form
        amis.addTreeForm(clazz);
        // 添加树边栏
        amis.addCommonTreeAside(clazz, properties);
        stopWatch.stop();
        log.debug("tree模板：{}", JSONUtil.toJSONString(amis));
        log.debug("tree模板生成耗时：{}ms", stopWatch.getTotalTimeMillis());
        return amis;
    }

    public static Map<String, Object> getTreeM2mTemplate(Class<?> clazz, Map<String, Object> properties) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Amis amis = JSONUtil.parseObject(getTemplateUrl("m2mTreeTemplate.json"), Amis.class);
        stopWatch.stop();
        log.debug("crud模板：{}", JSONUtil.toJSONString(amis));
        log.debug("crud模板生成耗时：{}ms", stopWatch.getTotalTimeMillis());
        return amis;
    }

    /**
     * 得到模板
     *
     * @param templateName 模板名称
     * @return {@link JSONObject}
     */
    public static Map<String, Object> getTemplate(String templateName) {
        if (templateName == null) {
            return null;
        }
        //fifoCache
        if (FIFO_CACHE.containsKey(templateName)) {
            return (Map<String, Object>) JSONUtil.copyObject(FIFO_CACHE.get(templateName));
        }

        Map<String, Object> map = JSONUtil.parseObject(getTemplateUrl(templateName), Map.class);
        FIFO_CACHE.put(templateName, JSONUtil.copyObject(map));
        return map;
    }

    /**
     * 获取树多选择
     *
     * @param clazz      clazz
     * @param properties 属性
     * @return {@link Map}<{@link String} ,{@link Object}>
     */
    public static Map<String ,Object> getTreeMultiSelect(Class<?> clazz, Map<String, Object> properties){
        Map<String, Object> template = getTemplate("item/tree-input.json");
        Amis amis = JSONUtil.parseObject(template, Amis.class);
        // 添加form
        return amis;
    }
    /**
     * 得到模板
     *
     * @param templateName 模板名称
     * @return {@link JSONObject}
     */
    public static JSONArray getTemplateArray(String templateName) {
        if (templateName == null) {
            return null;
        }
        if (FIFO_CACHE.containsKey(templateName)) {
            return (JSONArray) JSON.copy(FIFO_CACHE.get(templateName));
        }
        JSONArray jsonArray = JSON.parseObject(getTemplateUrl(templateName), JSONArray.class);
        FIFO_CACHE.put(templateName, JSON.copy(jsonArray));
        return jsonArray;
    }

    /**
     * 获取模板的url
     *
     * @param templateName 模板名称
     * @return {@link URL}
     */
    private static URL getTemplateUrl(String templateName) {
        return ResourceUtil.getResource("/WEB-INF/amis/" + templateName);
    }

    public static void main(String[] args) {
        Map<String, Object> template = TemplateUtil.getTemplate("crudTemplate.json");
        System.out.println(template);
    }


}
