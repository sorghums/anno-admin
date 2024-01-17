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
import site.sorghum.anno.anno.tpl.BaseTplRender;

import java.util.HashMap;

@Controller
@SaIgnore
@Mapping(value = AnnoConstants.BASE_URL)
public class PageController {
    @Mapping(value = "/goAnnoSinglePage/{clazz}")
    public void goAnnoSinglePage(Context ctx, @Path String clazz,
                                 @Param(value = "tokenValue", required = true) String tokenValue) {
        ctx.redirect(AnnoConstants.BASE_URL + "/index.html#/anView/anViewList/" + clazz + "?&__tokenValue__=" + tokenValue + "&__full__=true");
    }

    @Mapping(value = "/annoTpl")
    public ModelAndView annoTpl(Context ctx, @Param String _tplId, @Param String _tplClassName) {
        BaseTplRender render = BaseTplRender.getClone(_tplId, _tplClassName);
        if (render == null) {
            throw new BizException("未找到渲染器");
        }
        // 注入参数 保留原参数
        render.addProps(new HashMap<>(ctx.paramMap()));
        // 执行函数
        render.hook();
        return new ModelAndView(render.getViewName(), render.getProps());
    }
}
