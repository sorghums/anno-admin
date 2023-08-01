package site.sorghum.anno.spring.controller;

import cn.dev33.satoken.annotation.SaIgnore;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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

        response.sendRedirect("/index#/login");
    }

    @GetMapping(value = "/index")
    public ModelAndView index() {
        return new ModelAndView("index.html");
    }

}
