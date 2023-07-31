package site.sorghum.anno.modular.file;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.noear.solon.Utils;
import site.sorghum.anno.common.config.AnnoProperty;


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
        String guid = Utils.guid();
        String localFilePath = annoProperty.getLocalFilePath();
        String fileName = fileInfo.getFileName();
        String suffix = FileNameUtil.getSuffix(fileName);
        String originalPath = fileInfo.getOriginalPath();
        byte[] bytes = fileInfo.getBytes();
        // 获取年
        String year = String.valueOf(DateUtil.thisYear());
        // 获取月
        String month = String.valueOf(DateUtil.thisMonth());
        // 获取日
        String day = String.valueOf(DateUtil.thisDayOfMonth());
        String fileUrl = AnFileService.joinPath( "anLocal",originalPath, year, month, day, guid + "." + suffix);
        FileUtil.writeBytes(bytes, localFilePath + fileName);
        fileInfo.setFileUrl(fileUrl);
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
