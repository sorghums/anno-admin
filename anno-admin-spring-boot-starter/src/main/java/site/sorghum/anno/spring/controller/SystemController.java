package site.sorghum.anno.spring.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.pre.plugin.controller.SystemBaseController;
import site.sorghum.anno.pre.plugin.entity.response.CaptchaResponse;

import java.util.Map;

/**
 * 系统控制器
 *
 * @author Sorghum
 * @since 2023/05/30
 */
@RestController
public class SystemController extends SystemBaseController {

    // 验证码
    @GetMapping(value = "/system/common/captcha")
    @SaIgnore
    public AnnoResult<CaptchaResponse> captcha() {
        return super.captcha();
    }

    @PostMapping(value = "/api/upload")
    public AnnoResult<Map<String,Object>> upload(MultipartFile file, HttpServletRequest request) throws Exception {
        return super.uploadFile(file.getInputStream(),file.getOriginalFilename());
    }

    @PostMapping(value = "/api/upload/file")
    public AnnoResult<Map<String,Object>> uploadFile(MultipartFile file, HttpServletRequest request) throws Exception {
        return super.uploadFile(file.getInputStream(),file.getOriginalFilename());
    }
}
