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

import java.net.URL;

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
    public static JSONObject getCrudTemplate(Class<?> clazz, JSONObject properties){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Amis amis = getTemplate("crudTemplate.json").toJavaObject(Amis.class);
        // 添加过滤
        amis.addCrudFilter(clazz);
        // 添加列
        amis.addCrudColumns(clazz);
        // 添加删除信息
        amis.addCrudDeleteButton(clazz);
        // 添加编辑信息
        amis.addCrudEditInfo(clazz);
        // 添加自定义按钮信息
        amis.addCrudColumnButtonInfo(clazz);
        // 添加新增信息
        amis.addCrudAddInfo(clazz);
        // 添加树边栏
        amis.addCommonTreeAside(clazz);
        stopWatch.stop();
        log.debug("crud模板：{}",amis.toJSONString());
        log.debug("crud模板生成耗时：{}ms",stopWatch.getTotalTimeMillis());
        return amis;
    }

    /**
     * 得到crud模板
     *
     * @param clazz      clazz
     * @param properties 页面参数
     * @return {@link JSONObject}
     */
    public static JSONObject getCrudM2mTemplate(Class<?> clazz, JSONObject properties){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Amis amis = getTemplate("m2mTemplate.json").toJavaObject(Amis.class);
//         添加过滤
//        amis.addCrudFilter(clazz);
        // 添加列
        amis.addCrudColumns(clazz);
        // 添加关联查询的列信息
        amis.addRelationCrudColumns(clazz);
//        // 添加删除信息
//        amis.addCrudDeleteButton(clazz);
        // 添加编辑信息
        amis.addCrudEditInfo(clazz);
        // 添加删除对应关联关系信息的按钮
        amis.addDeleteRelationEditInfo(clazz);
//        // 添加自定义按钮信息
//        amis.addCrudColumnButtonInfo(clazz);
        // 添加新增信息
//        amis.addCrudAddInfo(clazz);
//        // 添加树边栏
//        amis.addCommonTreeAside(clazz);
        stopWatch.stop();
        log.debug("crud模板：{}",amis.toJSONString());
        log.debug("crud模板生成耗时：{}ms",stopWatch.getTotalTimeMillis());
        return amis;
    }
    /**
     * 得到crud模板
     *
     * @param clazz      clazz
     * @param properties 页面参数
     * @return {@link JSONObject}
     */
    public static JSONObject getTreeTemplate(Class<?> clazz, JSONObject properties){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Amis amis = getTemplate("treeTemplate.json").toJavaObject(Amis.class);
        // 添加form
        amis.addTreeForm(clazz);
        // 添加树边栏
        amis.addCommonTreeAside(clazz);
        stopWatch.stop();
        log.debug("tree模板：{}",amis.toJSONString());
        log.debug("tree模板生成耗时：{}ms",stopWatch.getTotalTimeMillis());
        return amis;
    }
    /**
     * 得到模板
     *
     * @param templateName 模板名称
     * @return {@link JSONObject}
     */
    public static JSONObject getTemplate(String templateName){
        if (templateName == null) {
            return null;
        }
        //fifoCache
        if (FIFO_CACHE.containsKey(templateName)) {
            return (JSONObject)JSON.copy(FIFO_CACHE.get(templateName));
        }
        JSONObject jsonObject = JSON.parseObject(getTemplateUrl(templateName));
        FIFO_CACHE.put(templateName, JSON.copy(jsonObject));
        return jsonObject;
    }

    /**
     * 得到模板
     *
     * @param templateName 模板名称
     * @return {@link JSONObject}
     */
    public static JSONArray getTemplateArray(String templateName){
        if (templateName == null) {
            return null;
        }
        if (FIFO_CACHE.containsKey(templateName)) {
            return (JSONArray)JSON.copy(FIFO_CACHE.get(templateName));
        }
        JSONArray jsonArray = JSON.parseObject(getTemplateUrl(templateName), JSONArray.class);
        FIFO_CACHE.put(templateName,JSON.copy(jsonArray));
        return jsonArray;
    }

    /**
     * 获取模板的url
     *
     * @param templateName 模板名称
     * @return {@link URL}
     */
    private static URL getTemplateUrl(String templateName){
        return ResourceUtil.getResource("/WEB-INF/amis/"+templateName);
    }

    public static void main(String[] args) {
        JSONObject template = TemplateUtil.getTemplate("crudTemplate.json");
        System.out.println(template);
    }

}
