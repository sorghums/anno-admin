package site.sorghum.anno.spring.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;

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
        if (StpUtil.isLogin()){
            response.sendRedirect("/index.html#/home/index");
            return;
        }
        response.sendRedirect("/index.html#/login");
    }

    @GetMapping(value = "/goAnnoSinglePage/{clazz}")
    public void goAnnoSinglePage(HttpServletResponse response,@PathVariable String clazz) throws IOException {
        response.sendRedirect("/index.html#/amisSingle/index/" + clazz);
    }

}
