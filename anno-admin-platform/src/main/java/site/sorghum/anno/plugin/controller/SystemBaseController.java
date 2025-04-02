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
 * 系统基础控制器
 * <br>处理系统级功能如验证码、文件上传和全局配置获取
 */
@Named
public class SystemBaseController {

    @Inject
    private CaptchaManager captchaManager;

    @Inject
    private AnFileService anFileService;

    @Inject
    private AnPlatformService anPlatformService;

    /**
     * 生成验证码
     * @return 验证码响应(包含验证码图片和key)
     */
    public AnnoResult<CaptchaResponse> captcha() {
        CaptchaResponse captcha = captchaManager.createImageCaptcha();
        return AnnoResult.succeed(captcha);
    }

    /**
     * 上传文件(字节数组方式)
     * @param bytes 文件字节数组
     * @param fileName 文件名
     * @return 文件URL
     */
    public AnnoResult<Map<String, Object>> uploadFile(byte[] bytes, String fileName) {
        FileInfo fileInfo = buildFileInfo(bytes, fileName);
        fileInfo = anFileService.uploadFile(fileInfo);
        return AnnoResult.succeed(MapUtil.of("value", fileInfo.getFileUrl()));
    }

    /**
     * 上传文件(输入流方式)
     * @param inputStream 文件输入流
     * @param fileName 文件名
     * @return 文件URL
     */
    public AnnoResult<String> uploadFile(InputStream inputStream, String fileName) {
        FileInfo fileInfo = buildFileInfo(inputStream, fileName);
        fileInfo = anFileService.uploadFile(fileInfo);
        return AnnoResult.succeed(fileInfo.getFileUrl());
    }

    /**
     * 获取全局配置
     * @return 全局平台配置
     */
    public AnnoResult<AnPlatform> getGlobalConfig() {
        AnPlatform config = anPlatformService.queryGlobalAnPlatform();
        return AnnoResult.succeed(config);
    }

    /**
     * 构建文件信息对象(字节数组方式)
     * @param bytes 文件字节数组
     * @param fileName 文件名
     * @return 文件信息对象
     */
    private FileInfo buildFileInfo(byte[] bytes, String fileName) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setBytes(bytes);
        fileInfo.setFileName(fileName);
        return fileInfo;
    }

    /**
     * 构建文件信息对象(输入流方式)
     * @param inputStream 文件输入流
     * @param fileName 文件名
     * @return 文件信息对象
     */
    private FileInfo buildFileInfo(InputStream inputStream, String fileName) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setInputStream(inputStream);
        fileInfo.setFileName(fileName);
        return fileInfo;
    }
}