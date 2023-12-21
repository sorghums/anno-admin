package site.sorghum.anno.anno.controller;

import jakarta.inject.Inject;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;

/**
 * 功能控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
public class AnEntityBaseController {

    @Inject
    protected MetadataManager metadataManager;

    public AnnoResult<AnEntity> anEntity(String clazz){
        return AnnoResult.succeed(metadataManager.getEntity(clazz));
    }
}
