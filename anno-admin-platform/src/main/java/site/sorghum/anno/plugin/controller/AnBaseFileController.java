package site.sorghum.anno.plugin.controller;

import cn.hutool.core.io.FileUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno.plugin.service.AnFileService;

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
