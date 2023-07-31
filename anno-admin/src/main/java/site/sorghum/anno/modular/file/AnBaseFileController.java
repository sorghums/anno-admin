package site.sorghum.anno.modular.file;

import cn.hutool.core.io.FileUtil;
import jakarta.inject.Inject;
import site.sorghum.anno.common.AnnoBeanUtils;
import site.sorghum.anno.common.config.AnnoProperty;

import java.io.File;

/**
 * 一个文件控制器
 *
 * @author Sorghum
 * @since 2023/07/31
 */
public class AnBaseFileController {

    @Inject
    AnnoProperty annoProperty;

    public File getFile(String path){
        AnnoProperty annoProperty = AnnoBeanUtils.getBean(AnnoProperty.class);
        String localFilePath = annoProperty.getLocalFilePath();
        String filePath = AnFileService.joinPath(localFilePath, path);
        return FileUtil.file(filePath);
    }
}
