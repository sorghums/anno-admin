package site.sorghum.anno.solon.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.map.MapUtil;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.UploadedFile;
import site.sorghum.anno.common.response.AnnoResult;
import site.sorghum.anno.modular.system.controller.SystemBaseController;
import site.sorghum.anno.modular.system.entity.response.CaptchaResponse;
import site.sorghum.anno.modular.system.manager.CaptchaManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统控制器
 *
 * @author Sorghum
 * @since 2023/05/30
 */
@Controller
public class SystemController extends SystemBaseController {

    // 验证码
    @Mapping(value = "/system/common/captcha")
    @SaIgnore
    public AnnoResult<CaptchaResponse> captcha() {
        return super.captcha();
    }

    @Mapping(value = "/api/upload", multipart=true)
    public AnnoResult<Map<String,Object>> upload(Context ctx) throws Exception {
        HashMap<String,Object> res = MapUtil.newHashMap();
        res.put("value","https://solon.noear.org/img/solon/favicon.png");
        UploadedFile file = ctx.file("file");
        return AnnoResult.succeed(res).withStatus(0);
    }

    @Mapping(value = "/api/upload/file", multipart=true)
    public AnnoResult<Map<String,Object>> uploadFile(Context ctx) throws Exception {
        HashMap<String,Object> res = MapUtil.newHashMap();
        res.put("value","https://solon.noear.org/img/solon/favicon.png");
        UploadedFile file = ctx.file("file");
        return AnnoResult.succeed(res).withStatus(0);
    }
}
