package site.sorghum.anno.spring.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno.auth.AnnoStpUtil;

import java.io.IOException;

/**
 * Pear控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Controller
@SaIgnore
public class WebController {

    @GetMapping(value = "/")
    public void first(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (AnnoStpUtil.isLogin()){
            response.sendRedirect(AnnoConstants.BASE_URL + "/index.html#/home/index");
            return;
        }
        response.sendRedirect(AnnoConstants.BASE_URL + "/index.html#/login");
    }

    @GetMapping(value = AnnoConstants.BASE_URL + "/goAnnoSinglePage/{clazz}")
    public void goAnnoSinglePage(HttpServletResponse response,@PathVariable String clazz) throws IOException {
        response.sendRedirect(AnnoConstants.BASE_URL + "/index.html#/amisSingle/index/" + clazz);
    }

}
