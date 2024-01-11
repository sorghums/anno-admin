package site.sorghum.anno.plugin.controller;

import cn.hutool.core.io.FileUtil;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Component;
import org.springframework.beans.factory.annotation.Autowired;
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
@Component
@org.springframework.stereotype.Component
public class AnBaseFileController {

    @Inject
    @Autowired
    AnnoProperty annoProperty;

    public InputStream getFile(String path) {
        String localFilePath = annoProperty.getLocalFilePath();
        String filePath = AnFileService.joinPath(localFilePath, path);
        File file = FileUtil.file(filePath);
        return FileUtil.getInputStream(file);
    }
}
