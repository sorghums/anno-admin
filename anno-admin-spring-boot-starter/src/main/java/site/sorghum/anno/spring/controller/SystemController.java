package site.sorghum.anno.spring.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.map.MapUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.pre.plugin.controller.SystemBaseController;
import site.sorghum.anno.pre.plugin.entity.response.CaptchaResponse;

import java.util.HashMap;
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

    @GetMapping(value = "/api/upload")
    public AnnoResult<Map<String,Object>> upload(MultipartFile file) throws Exception {
        HashMap<String,Object> res = MapUtil.newHashMap();
        res.put("value","https://solon.noear.org/img/solon/favicon.png");
        return AnnoResult.succeed(res).withStatus(0);
    }

    @GetMapping(value = "/api/upload/file")
    public AnnoResult<Map<String,Object>> uploadFile(MultipartFile file) throws Exception {
        HashMap<String,Object> res = MapUtil.newHashMap();
        res.put("value","https://solon.noear.org/img/solon/favicon.png");
        return AnnoResult.succeed(res).withStatus(0);
    }
}
