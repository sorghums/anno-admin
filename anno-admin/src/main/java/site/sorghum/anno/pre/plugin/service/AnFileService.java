package site.sorghum.anno.pre.plugin.service;

import site.sorghum.anno.pre.plugin.entity.common.FileInfo;

/**
 * An文件服务
 *
 * @author Sorghum
 * @since 2023/07/28
 */
public interface AnFileService {
    /**
     * 上传文件
     */
    FileInfo uploadFile(FileInfo fileInfo);

    /**
     * 获取文件信息
     */
    FileInfo getFileInfo(String key);

    /**
     * 获取文件url
     */
    String getUrl(String key);

    /**
     * 拼接路径
     * @param paths 路径
     * @return 拼接后的路径
     */
    public static String joinPath(String ...paths){
        StringBuilder sb = new StringBuilder();
        for (String path : paths) {
            sb.append(path);
            if(!path.endsWith("/")){
                sb.append("/");
            }
        }
        return sb.toString();
    }
}
