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
 * 一个文件控制器
 * 【anLocal】
 *
 * @author Sorghum
 * @since 2023/07/31
 */
@Named
@Slf4j
public class AnBaseFileController {

    @Inject
    AnnoProperty annoProperty;

    public InputStream getFile(String path) {
        String localFilePath = annoProperty.getLocalFilePath();
        String filePath = AnFileService.joinPath(localFilePath, path);
        File file = FileUtil.file(filePath);
        if (file.exists() && file.isFile()) {
            return FileUtil.getInputStream(file);
        }else {
            log.error("文件不存在：{}", filePath);
            return new ByteArrayInputStream(new byte[0]);
        }
    }
}
