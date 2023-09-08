package site.sorghum.anno.solon.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import org.noear.solon.annotation.Path;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.util.ClassUtil;
import site.sorghum.anno.anno.tpl.DefaultAnTplAction;

import java.util.HashMap;

@Controller
@SaIgnore
public class PageController {
    @Mapping(value = "/goAnnoSinglePage/{clazz}")
    public void goAnnoSinglePage(Context ctx, @Path String clazz,
                                 @Param(value = "tokenKey", required = true) String tokenKey,
                                 @Param(value = "tokenValue", required = true) String tokenValue) {
        ctx.redirect("/index.html#/amisSingle/index/" + clazz + "?tokenKey=" + tokenKey + "&tokenValue=" + tokenValue);
    }

    @Mapping(value = "/annoTpl/{tplActionClass}/{tplName}")
    public ModelAndView annoTpl(Context ctx, @Path String tplName, @Path String tplActionClass) {
        DefaultAnTplAction action;
        Class<?> tplClass = ClassUtil.loadClass(tplActionClass);
        if (tplClass == null) {
            action = Solon.context().getBean(DefaultAnTplAction.class);
        }
        action = (DefaultAnTplAction) Solon.context().getBean(tplClass);
        return new ModelAndView(tplName, action.data(new HashMap<>(ctx.paramMap())));
    }
}
