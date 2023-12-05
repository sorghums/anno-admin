package site.sorghum.anno.spring.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.plugin.controller.SystemBaseController;
import site.sorghum.anno.plugin.entity.response.CaptchaResponse;

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
    public AnnoResult<String> upload(MultipartFile file, HttpServletRequest request) throws Exception {
        return super.uploadFile(file.getInputStream(),file.getOriginalFilename());
    }


    @PostMapping(value = "/api/upload/file")
    public AnnoResult<String> uploadFile(MultipartFile file, HttpServletRequest request) throws Exception {
        return super.uploadFile(file.getInputStream(),file.getOriginalFilename());
    }

    @GetMapping(value = "/api/global/config")
    @SaIgnore
    public AnnoResult<Map<String ,Object>> getGlobalConfig(){
        return super.getGlobalConfig();
    }
}
