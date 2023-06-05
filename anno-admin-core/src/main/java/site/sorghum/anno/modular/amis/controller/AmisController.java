package site.sorghum.anno.modular.amis.controller;

import com.alibaba.fastjson2.JSONObject;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.util.AnnoClazzCache;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import site.sorghum.anno.modular.anno.util.TemplateUtil;

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
    public ModelAndView amis(String clazz) {
        Class<?> aClass = AnnoClazzCache.get(clazz);
        if (aClass == null) {
            return null;
        }
        AnnoMain annoMain = AnnoUtil.getAnnoMain(aClass);
        JSONObject crudTemplate = null;
        JSONObject properties = new JSONObject(){{
            put("clazz", clazz);
            put("treeClazz",AnnoUtil.getTreeClass(aClass));
        }};
        // 添加树类
        if (annoMain.annoTree().enable() && annoMain.annoTree().displayAsTree()){
            crudTemplate = TemplateUtil.getTreeTemplate(aClass,properties);
        }
        if (crudTemplate == null){
            crudTemplate = TemplateUtil.getCrudTemplate(aClass,properties);
        }
        ModelAndView modelAndView = new ModelAndView("function.html");
        modelAndView.put("amisJSON", crudTemplate);
        modelAndView.put("properties", properties);
        return modelAndView;
    }


}
