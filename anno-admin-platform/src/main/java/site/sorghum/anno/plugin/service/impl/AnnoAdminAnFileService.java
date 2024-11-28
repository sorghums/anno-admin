package site.sorghum.anno.plugin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno.file.AnFileService;
import site.sorghum.anno.file.FileInfo;

import java.util.List;


/**
 * An本地文件服务
 *
 * @author Sorghum
 * @since 2023/07/28
 */
@Named
public class AnnoAdminAnFileService implements AnFileService {

    @Inject
    private AnnoProperty annoProperty;

    /**
     * 实际服务
     */
    private AnFileService actualService = null;

    /**
     * 查找实际服务
     */
    private boolean findActualService = true;


    @Override
    public FileInfo upload(FileInfo fileInfo) {
        findActualService();
        if (actualService != null){
            return actualService.upload(fileInfo);
        }
        String guid = IdUtil.fastUUID();
        String localFilePath = annoProperty.getLocalFilePath();
        String fileName = fileInfo.getFileName();
        String suffix = FileNameUtil.getSuffix(fileName);
        String originalPath = StrUtil.isBlank(fileInfo.getOriginalPath()) ? "common" : fileInfo.getOriginalPath();
        byte[] bytes = fileInfo.getBytes();
        // 获取年
        String year = String.valueOf(DateUtil.thisYear());
        // 获取月
        String month = String.valueOf(DateUtil.thisMonth() + 1);
        // 获取日
        String day = String.valueOf(DateUtil.thisDayOfMonth());
        String fileUrl = AnFileService.joinPath("anLocal", originalPath, year, month, day, guid + "." + suffix);
        FileUtil.writeBytes(bytes, fileUrl);
        fileInfo.setFileUrl(AnFileService.joinPath(annoProperty.getApiServerUrl(),AnnoConstants.BASE_URL, fileUrl));
        return fileInfo;
    }

    @Override
    public FileInfo getFileInfo(String id) {
        findActualService();
        if (actualService != null){
            return actualService.getFileInfo(id);
        }
        throw new UnsupportedOperationException("不支持本地文件服务");
    }

    @Override
    public String getUrl(String key) {
        findActualService();
        if (actualService != null){
            return actualService.getUrl(key);
        }
        throw new UnsupportedOperationException("不支持本地文件服务");
    }

    private void findActualService(){
        if (findActualService){
            List<AnFileService> beansOfType = AnnoBeanUtils.getBeansOfType(AnFileService.class);
            for (AnFileService anFileService : beansOfType) {
                if (anFileService != this){
                    actualService = anFileService;
                    findActualService = false;
                }
            }
        }
    }
}
