package site.sorghum.anno.plugin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Component;
import org.springframework.beans.factory.annotation.Autowired;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno.plugin.entity.common.FileInfo;
import site.sorghum.anno.plugin.service.AnFileService;

import java.util.List;


/**
 * An本地文件服务
 *
 * @author Sorghum
 * @since 2023/07/28
 */
@Component
@org.springframework.stereotype.Component
public class LocalAnFileServiceImpl implements AnFileService {
    @Inject
    @Autowired
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
    public FileInfo uploadFile(FileInfo fileInfo) {
        findActualService();
        if (actualService != null){
            return actualService.uploadFile(fileInfo);
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
                }
            }
        }
    }
}
