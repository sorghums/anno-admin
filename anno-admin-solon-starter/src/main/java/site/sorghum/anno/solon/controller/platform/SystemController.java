package site.sorghum.anno.solon.controller.platform;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.UploadedFile;
import site.sorghum.anno.AnnoPlatform;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.plugin.ao.AnPlatform;
import site.sorghum.anno.plugin.controller.SystemBaseController;
import site.sorghum.anno.plugin.entity.response.CaptchaResponse;

import java.util.List;
import java.util.Map;

/**
 * 系统控制器
 *
 * @author Sorghum
 * @since 2023/05/30
 */
@Controller
@Api(tags = "系统控制器")
@Condition(onClass = AnnoPlatform.class)
@Mapping(value = AnnoConstants.BASE_URL)
public class SystemController {

    @Inject
    SystemBaseController systemBaseController;

    // 验证码
    @Mapping(value = "/system/common/captcha")
    @SaIgnore
    @Get
    @ApiOperation(value = "获取验证码", notes = "获取验证码")
    public AnnoResult<CaptchaResponse> captcha() {
        return systemBaseController.captcha();
    }

    @Mapping(value = "/api/upload",multipart = true)
    @Post
    @ApiOperation(value = "文件上传", notes = "文件上传")
    public AnnoResult<String> upload(UploadedFile file,Context context) throws Exception {
        Map<String, List<UploadedFile>> stringListMap = context.fileMap().toValuesMap();
        if (file == null && CollUtil.isNotEmpty(stringListMap)){
            List<UploadedFile> next = stringListMap.values().iterator().next();
            if (CollUtil.isNotEmpty(next)){
                file = next.get(0);
            }
        }
        if(file == null){
            return AnnoResult.failure("未读取到有效文件");
        }
        return systemBaseController.uploadFile(file.getContent(),file.getName());
    }

    @Mapping(value = "/api/upload/file")
    @Post
    @ApiOperation(value = "文件上传", notes = "文件上传")
    public AnnoResult<String> uploadFile(Context ctx) throws Exception {
        UploadedFile filed = ctx.file("file");
        return systemBaseController.uploadFile(filed.getContent(),filed.getName());
    }

    @Mapping(value = "/api/global/config")
    @SaIgnore
    public AnnoResult<AnPlatform> getGlobalConfig(){
        return systemBaseController.getGlobalConfig();
    }
}
