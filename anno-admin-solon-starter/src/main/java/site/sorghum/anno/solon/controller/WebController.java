package site.sorghum.anno.solon.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import org.noear.solon.annotation.Path;
import org.noear.solon.core.handle.Context;

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
        if (StpUtil.isLogin()) {
            ctx.redirect("/index.html#/home/index");
            return;
        }
        ctx.redirect("/index.html#/login");
    }

    @Mapping(value = "/goAnnoSinglePage/{clazz}")
    public void goAnnoSinglePage(Context ctx, @Path String clazz,
                                 @Param(value = "tokenKey", required = true) String tokenKey,
                                 @Param(value = "tokenValue", required = true) String tokenValue) {
        ctx.redirect("/index.html#/amisSingle/index/" + clazz + "?tokenKey=" + tokenKey + "&tokenValue=" + tokenValue);
    }

}
