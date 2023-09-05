package site.sorghum.anno.solon.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.UploadedFile;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.pre.plugin.controller.SystemBaseController;
import site.sorghum.anno.pre.plugin.entity.response.CaptchaResponse;

import java.util.List;
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

    @Mapping(value = "/api/upload",multipart = true)
    @Post
    public AnnoResult<Map<String,Object>> upload(UploadedFile file,Context context) throws Exception {
        Map<String, List<UploadedFile>> stringListMap = context.filesMap();
        return super.uploadFile(file.getContent(),file.getName());
    }

    @Mapping(value = "/api/upload/file")
    public AnnoResult<Map<String,Object>> uploadFile(Context ctx) throws Exception {
        UploadedFile filed = ctx.file("file");
        return super.uploadFile(filed.getContent(),filed.getName());
    }

    @Mapping(value = "/api/global/config")
    @SaIgnore
    public AnnoResult<Map<String ,Object>> getGlobalConfig(){
        return super.getGlobalConfig();
    }
}
