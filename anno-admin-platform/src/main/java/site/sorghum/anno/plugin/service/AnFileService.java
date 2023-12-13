package site.sorghum.anno.plugin.service;

import site.sorghum.anno.plugin.entity.common.FileInfo;

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
    static String joinPath(String... paths){
        StringBuilder sb = new StringBuilder();
        for (String path : paths) {
            sb.append(path);
            if(!path.endsWith("/")){
                sb.append("/");
            }
        }
        // 如果最后一个为 / , 去除最后一个/
        if(sb.length() > 1 && sb.charAt(sb.length() - 1) == '/'){
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
