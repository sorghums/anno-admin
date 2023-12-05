package site.sorghum.anno.solon.controller.platform;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Context;
import site.sorghum.anno.AnnoPlatform;
import site.sorghum.anno._common.AnnoConstants;

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
        if (StpUtil.isLogin()) {
            ctx.redirect(AnnoConstants.BASE_URL + "/index.html#/home/index");
            return;
        }
        ctx.redirect(AnnoConstants.BASE_URL + "/index.html#/login");
    }

}
