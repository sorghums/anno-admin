package site.sorghum.anno.modular.amis.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import site.sorghum.anno.common.response.AnnoResult;
import site.sorghum.anno.common.util.JSONUtil;
import site.sorghum.anno.metadata.AnEntity;
import site.sorghum.anno.metadata.MetadataManager;
import site.sorghum.anno.modular.anno.util.AnnoClazzCache;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import site.sorghum.anno.modular.anno.util.TemplateUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Controller
@Mapping(value = "/system/config")
public class AmisController {

    @Inject
    MetadataManager metadataManager;

    @Mapping(value = "/amis/{clazz}")
    @SaIgnore
    public ModelAndView amis(String clazz, Context context) {
        if(!StpUtil.isLogin()){
            context.redirect("/error/403.html");
            return null;
        };
        HashMap<String, Object> data = new HashMap<>(context.paramMap());
        AnEntity anEntity = metadataManager.getEntity(clazz);
        Object template = null;
        Map<String ,Object> properties = new HashMap<String,Object>(){{
            put("clazz", clazz);
            put("treeClazz",AnnoUtil.getTreeClass(anEntity.getClazz()));
            this.putAll(data);
            put("extraData", JSON.toJSONString(data));
        }};
        // 添加树类
        if (anEntity.isEnableTree() && anEntity.isTreeDisplayAsTree()){
            template = TemplateUtil.getTreeTemplate(anEntity.getClazz(),properties);
        }
        // 添加crud类
        if (template == null){
            template = TemplateUtil.getCrudTemplate(anEntity.getClazz(),properties);
        }
        ModelAndView modelAndView = new ModelAndView("function.html");
        modelAndView.put("amisJSON", template);
        modelAndView.put("properties", properties);
        return modelAndView;
    }

    @Mapping(value = "/amisJson/{clazz}")
    @SaIgnore
    public AnnoResult<Object> amisJson(String clazz, Context context) {
        HashMap<String, Object> data = new HashMap<>(context.paramMap());
        AnEntity anEntity = metadataManager.getEntity(clazz);
        Object template = null;
        Map<String ,Object> properties = new HashMap<String,Object>(){{
            put("clazz", clazz);
            put("treeClazz",AnnoUtil.getTreeClass(anEntity.getClazz()));
            this.putAll(data);
            put("extraData", JSON.toJSONString(data));
        }};
        // 添加树类
        if (anEntity.isEnableTree() && anEntity.isTreeDisplayAsTree()){
            template = TemplateUtil.getTreeTemplate(anEntity.getClazz(),properties);
        }
        // 添加crud类
        if (template == null){
            template = TemplateUtil.getCrudTemplate(anEntity.getClazz(),properties);
        }
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("amisJSON", JSONUtil.toBean(template,Map.class));
        returnMap.put("properties", JSONUtil.toBean(properties,Map.class));
        return AnnoResult.succeed(returnMap);
    }

    @Mapping(value = "/amis-m2m/{clazz}")
    @SaIgnore
    public ModelAndView amisM2m(String clazz, Context context) {
        if(!StpUtil.isLogin()){
            context.redirect("/error/403.html");
            return null;
        };
        HashMap<String, Object> data = new HashMap<>(context.paramMap());
        Class<?> aClass = AnnoClazzCache.get(clazz);
        if (aClass == null) {
            return null;
        }
        AnEntity anEntity = metadataManager.getEntity(clazz);
        Object template = null;
        Map<String ,Object> properties = new HashMap<String,Object>(){{
            put("clazz", clazz);
            put("treeClazz",AnnoUtil.getTreeClass(aClass));
            this.putAll(data);
            put("extraData", JSON.toJSONString(data));
        }};
        // 添加树类
        if (anEntity.isEnableTree() && anEntity.isTreeDisplayAsTree()){
            template = TemplateUtil.getTreeM2mTemplate(aClass,properties);
        }
        // 添加crud类
        if (template == null){
            template = TemplateUtil.getCrudM2mTemplate(aClass,properties);
        }
        ModelAndView modelAndView = new ModelAndView("function.html");
        modelAndView.put("amisJSON", template);
        modelAndView.put("properties", properties);
        return modelAndView;
    }

}
