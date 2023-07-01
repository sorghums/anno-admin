package site.sorghum.anno.modular.amis.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.util.AnnoClazzCache;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import site.sorghum.anno.modular.anno.util.TemplateUtil;
import site.sorghum.anno.util.JSONUtil;

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


    @Mapping(value = "/amis/{clazz}")
    @SaIgnore
    public ModelAndView amis(String clazz, Context context) {
        if(!StpUtil.isLogin()){
            context.redirect("/error/403.html");
            return null;
        };
        HashMap<String, Object> data = new HashMap<>(context.paramMap());
        Class<?> aClass = AnnoClazzCache.get(clazz);
        if (aClass == null) {
            return null;
        }
        AnnoMain annoMain = AnnoUtil.getAnnoMain(aClass);
        Map<String ,Object> template = null;
        Map<String ,Object> properties = new HashMap<String,Object>(){{
            put("clazz", clazz);
            put("treeClazz",AnnoUtil.getTreeClass(aClass));
            this.putAll(data);
            put("extraData", JSON.toJSONString(data));
        }};
        // 添加树类
        if (annoMain.annoTree().enable() && annoMain.annoTree().displayAsTree()){
            template = TemplateUtil.getTreeTemplate(aClass,properties);
        }
        // 添加crud类
        if (template == null){
            template = TemplateUtil.getCrudTemplate(aClass,properties);
        }
        ModelAndView modelAndView = new ModelAndView("function.html");
        modelAndView.put("amisJSON", JSONUtil.toJSONString(template));
        modelAndView.put("properties", properties);
        return modelAndView;
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
        AnnoMain annoMain = AnnoUtil.getAnnoMain(aClass);
        Map<String ,Object> template = null;
        Map<String ,Object> properties = new HashMap<String,Object>(){{
            put("clazz", clazz);
            put("treeClazz",AnnoUtil.getTreeClass(aClass));
            this.putAll(data);
            put("extraData", JSON.toJSONString(data));
        }};
        // 添加树类
        if (annoMain.annoTree().enable() && annoMain.annoTree().displayAsTree()){
            // TODO 添加m2m 树模板
            template = TemplateUtil.getTreeM2mTemplate(aClass,properties);
        }
        // 添加crud类
        if (template == null){
            template = TemplateUtil.getCrudM2mTemplate(aClass,properties);
        }
        ModelAndView modelAndView = new ModelAndView("function.html");
        modelAndView.put("amisJSON", JSONUtil.toJSONString(template));
        modelAndView.put("properties", properties);
        return modelAndView;
    }

}
