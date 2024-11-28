package site.sorghum.anno.file;


import site.sorghum.anno._common.AnnoBeanUtils;

import java.util.List;

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
    FileInfo upload(FileInfo fileInfo);

    /**
     * 上传文件[内部使用 请勿修改]
     * @param fileInfo 文件信息
     * @return 文件信息
     */
    default FileInfo uploadFile(FileInfo fileInfo){
        List<FileProxy> beansOfType = AnnoBeanUtils.getBeansOfType(FileProxy.class);
        for (FileProxy bean : beansOfType) {
            bean.beforeUpdate(fileInfo);
        }
        upload(fileInfo);
        for (FileProxy bean : beansOfType) {
            bean.afterUpdate(fileInfo);
        }
        return fileInfo;
    }

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
