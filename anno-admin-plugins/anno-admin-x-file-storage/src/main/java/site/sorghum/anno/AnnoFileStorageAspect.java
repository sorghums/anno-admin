package site.sorghum.anno;

import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno.file.FileProxy;

@Slf4j
public class AnnoFileStorageAspect implements FileProxy {

    @Override
    public void beforeUpdate(site.sorghum.anno.file.FileInfo fileInfo) {

    }

    @Override
    public void afterUpdate(site.sorghum.anno.file.FileInfo fileInfo) {
    }
}
