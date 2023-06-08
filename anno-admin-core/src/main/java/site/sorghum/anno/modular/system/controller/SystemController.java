package site.sorghum.anno.modular.system.controller;

import com.alibaba.fastjson2.JSONObject;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.UploadedFile;
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
public class SystemController {

    @Inject
    CaptchaManager captchaManager;

    // 验证码
    @Mapping(value = "/system/common/captcha")
    public AnnoResult<CaptchaResponse> captcha() {
        return AnnoResult.succeed(captchaManager.createImageCaptcha());
    }

    @Mapping(value = "/api/upload", multipart=true)
    public AnnoResult<JSONObject> upload(Context ctx) throws Exception {
        JSONObject res = new JSONObject();
        res.put("value","https://solon.noear.org/img/solon/favicon.png");
        UploadedFile file = ctx.file("file");
        return AnnoResult.succeed(res).withStatus(0);
    }
}
