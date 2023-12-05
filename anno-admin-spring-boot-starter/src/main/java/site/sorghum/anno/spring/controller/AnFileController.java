package site.sorghum.anno.spring.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.io.IoUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.sorghum.anno.plugin.controller.AnBaseFileController;

import java.io.InputStream;

@RestController
@RequestMapping(value = "anLocal")
public class AnFileController extends AnBaseFileController {

    @SneakyThrows
    @GetMapping(value = "/**")
    @SaIgnore
    public void getFile(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletPath();
        // 获取anLocal后面的路径
        path = path.substring(path.indexOf("anLocal") + 7);
        InputStream inputStream
                = super.getFile(path);
        IoUtil.write(response.getOutputStream(), true, IoUtil.readBytes(inputStream));
    }
}
