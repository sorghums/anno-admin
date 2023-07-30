package site.sorghum.anno.solon.controller;

import cn.dev33.satoken.annotation.SaIgnore;
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
@SaIgnore
public class WebController {

    @Mapping(value = "/")
    public void first(Context ctx) {
        if (StpUtil.isLogin()){
            ctx.redirect("/index#/home/index");
            return;
        }
        ctx.redirect("/index#/login");
    }

    @Mapping(value = "/index")
    public ModelAndView index(Context ctx) {
        return new ModelAndView("index.html");
    }

}
