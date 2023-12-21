package site.sorghum.anno.solon.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import org.noear.solon.annotation.Path;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.tpl.TplRender;

@Controller
@SaIgnore
@Mapping(value = AnnoConstants.BASE_URL)
public class PageController {
    @Mapping(value = "/goAnnoSinglePage/{clazz}")
    public void goAnnoSinglePage(Context ctx, @Path String clazz,
                                 @Param(value = "tokenKey", required = true) String tokenKey,
                                 @Param(value = "tokenValue", required = true) String tokenValue) {
        ctx.redirect(AnnoConstants.BASE_URL + "/index.html#/amisSingle/index/" + clazz + "?tokenKey=" + tokenKey + "&tokenValue=" + tokenValue);
    }

    @Mapping(value = "/annoTpl")
    public ModelAndView annoTpl(Context ctx, @Param String tplId) {
        TplRender render = TplRender.getClone(tplId);
        if (render == null) {
            throw new BizException("未找到渲染器");
        }
        // 注入参数 保留原参数
        render.getProps().putAll(ctx.paramMap());
        return new ModelAndView(render.getView(),render.getProps());
    }
}
