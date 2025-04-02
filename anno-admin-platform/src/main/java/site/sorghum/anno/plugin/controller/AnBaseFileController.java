package site.sorghum.anno.plugin.controller;

import cn.hutool.core.io.FileUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno.file.AnFileService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * 文件基础控制器
 * 提供本地文件获取功能
 */
@Named
@Slf4j
public class AnBaseFileController {

    @Inject
    private AnnoProperty annoProperty;  // 应用配置属性

    /**
     * 获取文件输入流
     * @param path 文件相对路径
     * @return 文件输入流(文件不存在时返回空流)
     */
    public InputStream getFile(String path) {
        String fullPath = AnFileService.joinPath(annoProperty.getLocalFilePath(), path);
        File file = FileUtil.file(fullPath);

        if (!file.exists() || !file.isFile()) {
            log.error("文件不存在：{}", fullPath);
            return new ByteArrayInputStream(new byte[0]);
        }

        return FileUtil.getInputStream(file);
    }
}