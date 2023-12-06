package site.sorghum.anno.plugin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno.plugin.entity.common.FileInfo;
import site.sorghum.anno.plugin.service.AnFileService;


/**
 * An本地文件服务
 *
 * @author Sorghum
 * @since 2023/07/28
 */
@Named
public class LocalAnFileServiceImpl implements AnFileService {
    @Inject
    private AnnoProperty annoProperty;

    @Override
    public FileInfo uploadFile(FileInfo fileInfo) {
        String guid = IdUtil.fastUUID();
        String localFilePath = annoProperty.getLocalFilePath();
        String fileName = fileInfo.getFileName();
        String suffix = FileNameUtil.getSuffix(fileName);
        String originalPath = StrUtil.isBlank(fileInfo.getOriginalPath()) ? "common" : fileInfo.getOriginalPath();
        byte[] bytes = fileInfo.getBytes();
        // 获取年
        String year = String.valueOf(DateUtil.thisYear());
        // 获取月
        String month = String.valueOf(DateUtil.thisMonth());
        // 获取日
        String day = String.valueOf(DateUtil.thisDayOfMonth());
        String fileUrl = AnFileService.joinPath("anLocal", originalPath, year, month, day, guid + "." + suffix);
        FileUtil.writeBytes(bytes, fileUrl);
        fileInfo.setFileUrl(AnFileService.joinPath(annoProperty.getApiServerUrl(),AnnoConstants.BASE_URL, fileUrl));
        return fileInfo;
    }

    @Override
    public FileInfo getFileInfo(String id) {
        throw new UnsupportedOperationException("不支持本地文件服务");
    }

    @Override
    public String getUrl(String key) {
        throw new UnsupportedOperationException("不支持本地文件服务");
    }

}
