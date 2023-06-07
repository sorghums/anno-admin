package site.sorghum.anno.modular.system.controller;

import site.sorghum.anno.modular.system.entity.response.CaptchaResponse;
import site.sorghum.anno.modular.system.manager.CaptchaManager;
import site.sorghum.anno.response.AnnoResult;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;

/**
 * 系统控制器
 *
 * @author Sorghum
 * @since 2023/05/30
 */
@Controller
@Mapping(value = "/system/common")
public class SystemController {

    @Inject
    CaptchaManager captchaManager;

    // 验证码
    @Mapping(value = "/captcha")
    public AnnoResult<CaptchaResponse> captcha() {
        return AnnoResult.succeed(captchaManager.createImageCaptcha());
    }
}
