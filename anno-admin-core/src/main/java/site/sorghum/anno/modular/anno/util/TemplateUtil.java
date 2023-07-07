package site.sorghum.anno.modular.anno.util;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.FIFOCache;
import cn.hutool.core.date.StopWatch;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.core.util.ResourceUtil;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.anno.modular.amis.model.CrudM2mView;
import site.sorghum.anno.modular.amis.model.CrudView;
import site.sorghum.anno.modular.amis.model.TreeM2mView;
import site.sorghum.anno.modular.amis.model.TreeView;
import site.sorghum.anno.modular.amis.process.processer.CrudProcessorChain;
import site.sorghum.anno.util.JSONUtil;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * 模板工具
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Slf4j
public class TemplateUtil {

    /**
     * 得到crud模板
     *
     * @param clazz      clazz
     * @param properties 页面参数
     * @return {@link Map}
     */
    public static CrudView getCrudTemplate(Class<?> clazz, Map<String, Object> properties) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CrudProcessorChain crudProcessorChain = new CrudProcessorChain();
        AmisBaseWrapper wrapper = AmisBaseWrapper.of();
        crudProcessorChain.doProcessor(wrapper,clazz,properties);
        CrudView crudView = ((CrudView) wrapper.getAmisBase());
        stopWatch.stop();
        log.debug("crud模板：{}", JSONUtil.toJSONString(crudView));
        log.debug("crud模板生成耗时：{}ms", stopWatch.getTotalTimeMillis());
        return crudView;
    }

    /**
     * 得到crud模板
     *
     * @param clazz      clazz
     * @param properties 页面参数
     * @return {@link Map}
     */
    public static CrudM2mView getCrudM2mTemplate(Class<?> clazz, Map<String, Object> properties) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CrudM2mView crudM2mView = CrudM2mView.of();
        // 添加过滤
        crudM2mView.addCrudFilter(clazz);
        // 添加列
        crudM2mView.addCrudColumns(clazz);
        // 添加关联查询的表格信息
        properties.put("isM2m", true);
        crudM2mView.addRelationCrudData(clazz, getCrudTemplate(clazz, properties));
        // 添加编辑信息
        crudM2mView.addCrudEditInfo(clazz);
        // 添加删除对应关联关系信息的按钮
        crudM2mView.addDeleteRelationEditInfo(clazz);
        stopWatch.stop();
        log.debug("crud模板：{}", JSONUtil.toJSONString(crudM2mView));
        log.debug("crud模板生成耗时：{}ms", stopWatch.getTotalTimeMillis());
        return crudM2mView;
    }

    /**
     * 得到树模板
     *
     * @param clazz      clazz
     * @param properties 页面参数
     * @return {@link TreeView}
     */
    public static TreeView getTreeTemplate(Class<?> clazz, Map<String, Object> properties) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TreeView treeView = TreeView.of();
        // 添加form
        treeView.addTreeForm(clazz);
        // 添加树边栏
        treeView.addCommonTreeAside(clazz, properties);
        // 添加自定义按钮
        treeView.addTreeColumnButtonInfo(clazz);
        stopWatch.stop();
        log.debug("tree模板：{}", JSONUtil.toJSONString(treeView));
        log.debug("tree模板生成耗时：{}ms", stopWatch.getTotalTimeMillis());
        return treeView;
    }

    public static TreeM2mView getTreeM2mTemplate(Class<?> clazz, Map<String, Object> properties) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TreeM2mView treeM2mView = TreeM2mView.of();
        stopWatch.stop();
        log.debug("crud模板：{}", JSONUtil.toJSONString(treeM2mView));
        log.debug("crud模板生成耗时：{}ms", stopWatch.getTotalTimeMillis());
        return treeM2mView;
    }

}
