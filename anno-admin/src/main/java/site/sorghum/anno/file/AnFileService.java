package site.sorghum.anno.file;

import site.sorghum.anno._common.AnnoBeanUtils;

import java.util.Comparator;
import java.util.List;

/**
 * 文件服务接口
 * 提供文件上传、获取文件信息和URL等核心功能
 */
public interface AnFileService {

    /**
     * 上传文件核心方法
     * @param fileInfo 包含文件信息和数据的对象
     * @return 上传后的文件信息对象
     */
    FileInfo upload(FileInfo fileInfo);

    /**
     * 上传文件（包含代理处理）
     * @param fileInfo 文件信息对象
     * @return 处理后的文件信息对象
     */
    default FileInfo uploadFile(FileInfo fileInfo) {
        // 获取所有文件代理并按优先级排序
        List<FileProxy> fileProxies = AnnoBeanUtils.getBeansOfType(FileProxy.class);
        fileProxies.sort(Comparator.comparing(FileProxy::order));

        // 执行上传前处理
        for (FileProxy proxy : fileProxies) {
            proxy.beforeUpdate(fileInfo);
        }

        // 执行上传
        FileInfo uploadedFile = upload(fileInfo);

        // 执行上传后处理
        for (FileProxy proxy : fileProxies) {
            proxy.afterUpdate(uploadedFile);
        }

        return uploadedFile;
    }

    /**
     * 根据文件key获取文件信息
     * @param key 文件唯一标识
     * @return 文件信息对象
     */
    FileInfo getFileInfo(String key);

    /**
     * 根据文件key获取文件访问URL
     * @param key 文件唯一标识
     * @return 文件访问URL
     */
    String getUrl(String key);

    /**
     * 路径拼接工具方法
     * @param paths 多个路径片段
     * @return 拼接后的完整路径
     */
    static String joinPath(String... paths) {
        if (paths == null || paths.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String path : paths) {
            if (path == null || path.isEmpty()) {
                continue;
            }

            String trimmedPath = path.trim();
            if (!trimmedPath.isEmpty()) {
                sb.append(trimmedPath);
                if (!trimmedPath.endsWith("/")) {
                    sb.append("/");
                }
            }
        }

        // 移除末尾多余的斜杠
        if (!sb.isEmpty() && sb.charAt(sb.length() - 1) == '/') {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }
}