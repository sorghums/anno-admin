package site.sorghum.anno.file;

/**
 * 文件操作代理接口
 * 允许在文件上传前后执行自定义逻辑
 */
public interface FileProxy {

    /**
     * 获取代理执行顺序
     * @return 优先级数值，数值越小优先级越高
     */
    default Integer order() {
        return 0;
    }

    /**
     * 文件上传前处理
     * @param fileInfo 文件信息对象
     */
    void beforeUpdate(FileInfo fileInfo);

    /**
     * 文件上传后处理
     * @param fileInfo 文件信息对象
     */
    void afterUpdate(FileInfo fileInfo);
}