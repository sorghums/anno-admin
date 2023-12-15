package site.sorghum.anno.solon.controller.platform;

import cn.dev33.satoken.annotation.SaIgnore;
import org.noear.solon.annotation.Condition;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import site.sorghum.anno.AnnoPlatform;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno.auth.AnnoStpUtil;

/**
 * Pear控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Controller
@Condition(onClass = AnnoPlatform.class)
@SaIgnore
public class WebController {

    @Mapping(value = "/")
    public void first(Context ctx) {
        if (AnnoStpUtil.isLogin()) {
            ctx.redirect(AnnoConstants.BASE_URL + "/index.html#/home/index");
            return;
        }
        ctx.redirect(AnnoConstants.BASE_URL + "/index.html#/login");
    }

}
