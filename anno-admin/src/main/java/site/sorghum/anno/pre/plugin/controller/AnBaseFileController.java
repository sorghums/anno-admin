package site.sorghum.anno.pre.plugin.controller;

import cn.hutool.core.io.FileUtil;
import jakarta.inject.Inject;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno.pre.plugin.service.AnFileService;

import java.io.File;
import java.io.InputStream;

/**
 * 一个文件控制器
 * 【anLocal】
 *
 * @author Sorghum
 * @since 2023/07/31
 */
public class AnBaseFileController {

    @Inject
    AnnoProperty annoProperty;

    public InputStream getFile(String path) {
        String localFilePath = annoProperty.getLocalFilePath();
        String filePath = AnFileService.joinPath(localFilePath, path);
        File file = FileUtil.file(filePath);
        return FileUtil.getInputStream(file);
    }
}
