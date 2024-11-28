package site.sorghum.anno.file;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultFileProxy implements FileProxy {
    @Override
    public void beforeUpdate(FileInfo fileInfo) {
        log.info("beforeUpdate:{}", fileInfo);
    }

    @Override
    public void afterUpdate(FileInfo fileInfo) {
        log.info("afterUpdate:{}", fileInfo);
    }
}
