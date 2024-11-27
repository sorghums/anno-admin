package site.sorghum.anno.file;

import jakarta.inject.Named;

@Named
public interface FileProxy {

    /**
     * 获取文件代理的优先级
     * @return 优先级
     */
    default Integer order(){
        return 0;
    }

    /**
     * 更新文件之前执行
     * @param fileInfo 文件信息
     */
    void beforeUpdate(FileInfo fileInfo);

    /**
     * 更新文件之后执行
     * @param fileInfo 文件信息
     */
    void afterUpdate(FileInfo fileInfo);
}
