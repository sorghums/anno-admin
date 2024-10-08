package site.sorghum.anno.plugin.controller;

import cn.hutool.core.map.MapUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno.file.AnFileService;
import site.sorghum.anno.file.FileInfo;
import site.sorghum.anno.plugin.ao.AnPlatform;
import site.sorghum.anno.plugin.entity.response.CaptchaResponse;
import site.sorghum.anno.plugin.manager.CaptchaManager;
import site.sorghum.anno.plugin.service.AnPlatformService;

import java.io.InputStream;
import java.util.Map;

/**
 * 系统控制器
 *
 * @author Sorghum
 * @since 2023/05/30
 */
@Named
public class SystemBaseController {

    @Inject
    CaptchaManager captchaManager;

    @Inject
    AnFileService anFileService;

    @Inject
    AnPlatformService anPlatformService;

    // 验证码
    public AnnoResult<CaptchaResponse> captcha() {
        return AnnoResult.succeed(captchaManager.createImageCaptcha());
    }

    public AnnoResult<Map<String, Object>> uploadFile(byte[] bytes, String fileName) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setBytes(bytes);
        fileInfo.setFileName(fileName);
        fileInfo = anFileService.uploadFile(fileInfo);
        return AnnoResult.succeed(MapUtil.of("value", fileInfo.getFileUrl()));
    }

    public AnnoResult<String> uploadFile(InputStream inputStream, String fileName) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setInputStream(inputStream);
        fileInfo.setFileName(fileName);
        fileInfo = anFileService.uploadFile(fileInfo);
        return AnnoResult.succeed(fileInfo.getFileUrl());
    }

    public AnnoResult<AnPlatform> getGlobalConfig(){
        AnPlatform globalAnPlatform = anPlatformService.queryGlobalAnPlatform();
        return AnnoResult.succeed(globalAnPlatform);
    }

}
