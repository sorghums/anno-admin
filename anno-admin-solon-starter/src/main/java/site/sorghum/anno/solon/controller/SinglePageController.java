package site.sorghum.anno.solon.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import org.noear.solon.annotation.Path;
import org.noear.solon.core.handle.Context;

@Controller
@SaIgnore
public class SinglePageController {
    @Mapping(value = "/goAnnoSinglePage/{clazz}")
    public void goAnnoSinglePage(Context ctx, @Path String clazz,
                                 @Param(value = "tokenKey", required = true) String tokenKey,
                                 @Param(value = "tokenValue", required = true) String tokenValue) {
        ctx.redirect("/index.html#/amisSingle/index/" + clazz + "?tokenKey=" + tokenKey + "&tokenValue=" + tokenValue);
    }
}
