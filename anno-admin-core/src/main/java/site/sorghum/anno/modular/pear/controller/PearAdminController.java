package site.sorghum.anno.modular.pear.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;

/**
 * Pear控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Controller
public class PearAdminController {

    @Mapping(value = "/")
    public void first(Context ctx) {
        if (StpUtil.isLogin()){
            ctx.redirect("/index.html");
            return;
        }
        ctx.redirect("/login.html");
    }

    @Mapping(value = "login.html")
    public ModelAndView login(Context ctx) {
        return new ModelAndView("login.html");
    }

    @Mapping(value = "index.html")
    public ModelAndView index(Context ctx) {
        return new ModelAndView("index.html");
    }

    @Mapping(value = "/error/403.html")
    public ModelAndView error403() {
        return new ModelAndView("/error/403.html");
    }

    @Mapping(value = "/error/404.html")
    public ModelAndView error404() {
        return new ModelAndView("/error/404.html");
    }

    @Mapping(value = "/error/500.html")
    public ModelAndView error500() {
        return new ModelAndView("/error/500.html");
    }

}
