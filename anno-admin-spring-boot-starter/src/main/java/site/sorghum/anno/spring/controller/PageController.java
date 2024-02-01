package site.sorghum.anno.spring.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.io.file.FileNameUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.tpl.BaseTplRender;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@SaIgnore
@RequestMapping(value = AnnoConstants.BASE_URL)
public class PageController {
    @GetMapping(value = "/goAnnoSinglePage/{clazz}")
    public void goAnnoSinglePage(HttpServletResponse response, @PathVariable String clazz,
                                 @RequestParam(value = "tokenValue", required = true) String tokenValue) throws IOException {
        response.sendRedirect(AnnoConstants.BASE_URL + "/index.html#/anView/anViewList/" + clazz + "?&__tokenValue__=" + tokenValue + "&__full__=true");
    }

    @GetMapping(value = "/annoTpl")
    public ModelAndView annoTpl(HttpServletRequest request, @RequestParam(required = false) String _tplId, @RequestParam(required = false) String _tplClassName) {
        BaseTplRender render = BaseTplRender.getClone(_tplId, _tplClassName);
        if (render == null) {
            throw new BizException("未找到渲染器");
        }
        // 注入参数 保留原参数
        render.addProps(parseProps(request));
        // 执行函数
        render.hook();
        return new ModelAndView(FileNameUtil.getPrefix(render.getViewName()), render.getProps());
    }

    private Map<String,Object> parseProps(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        return parameterMap.entrySet().stream().collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()[0]), HashMap::putAll);
    }
}
