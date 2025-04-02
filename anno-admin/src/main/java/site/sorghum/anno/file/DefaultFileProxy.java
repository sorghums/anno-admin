package site.sorghum.anno.file;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认文件代理实现
 * 提供基本的日志记录功能
 */
@Slf4j
public class DefaultFileProxy implements FileProxy {

    @Override
    public void beforeUpdate(FileInfo fileInfo) {
        log.debug("准备上传文件: {}", fileInfo);
    }

    @Override
    public void afterUpdate(FileInfo fileInfo) {
        log.debug("文件上传完成: {}", fileInfo);
    }
}