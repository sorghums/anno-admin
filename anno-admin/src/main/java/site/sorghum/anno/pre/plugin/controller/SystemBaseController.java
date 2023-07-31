package site.sorghum.anno.pre.plugin.controller;

import jakarta.inject.Inject;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.pre.plugin.entity.response.CaptchaResponse;
import site.sorghum.anno.pre.plugin.manager.CaptchaManager;

/**
 * 系统控制器
 *
 * @author Sorghum
 * @since 2023/05/30
 */
public class SystemBaseController {

    @Inject
    CaptchaManager captchaManager;

    // 验证码
    public AnnoResult<CaptchaResponse> captcha() {
        return AnnoResult.succeed(captchaManager.createImageCaptcha());
    }

}
