package site.sorghum.anno;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.constant.Constant;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.file.AnFileService;
import site.sorghum.anno.file.FileInfo;

@Named
public class XFileService implements AnFileService {

    @Inject
    FileStorageService fileStorageService;

    /**
     * 删除文件额外参数
     * 适配[免鉴权]
     */
    public static Boolean removeUrl = false;

    /**
     * 文件权限
     */
    public static String defaultAcl = Constant.ACL.PUBLIC_READ;

    @Override
    public FileInfo uploadFile(FileInfo fileInfo) {
        if (fileStorageService == null) {
            throw new BizException("fileStorageService未初始化，请手动引入X-File-Storage包，注入FileStorageService的Bean到容器中.");
        }
        String buildKey = buildKey(fileInfo);
        org.dromara.x.file.storage.core.FileInfo aliFileInfo = fileStorageService
            .of(fileInfo.getBytes())
            .setPath(buildKey)
            .setName(fileInfo.getFileName())
            .setAcl(StrUtil.isBlank(fileInfo.getAcl()) ? defaultAcl : fileInfo.getAcl())
            .upload();
        fileInfo.setFileUrl(removeParam(aliFileInfo.getUrl()));
        return fileInfo;
    }

    @Override
    public FileInfo getFileInfo(String key) {
        return null;
    }

    @Override
    public String getUrl(String key) {
        return null;
    }

    private String buildKey(FileInfo fileInfo) {
        String originalPath = StrUtil.isBlank(fileInfo.getOriginalPath()) ? "common" : fileInfo.getOriginalPath();
        // 获取年
        String year = String.valueOf(DateUtil.thisYear());
        // 获取月
        String month = String.valueOf(DateUtil.thisMonth());
        // 获取日
        String day = String.valueOf(DateUtil.thisDayOfMonth());
        return AnFileService.joinPath("anno-admin", originalPath, year, month, day);
    }

    private String removeParam(String url) {
        if (!removeUrl) {
            return url;
        }
        if (StrUtil.isBlank(url)) {
            return url;
        }
        int index = url.indexOf("?");
        if (index > 0) {
            return url.substring(0, index);
        }
        return url;
    }
}
