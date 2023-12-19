package site.sorghum.anno.amis.controller;

import jakarta.inject.Inject;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;

import java.util.Map;

/**
 * 功能控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
public class AmisBaseController {

    @Inject
    protected MetadataManager metadataManager;

    public AnnoResult<AnEntity> anEntity(String clazz){
        return AnnoResult.succeed(metadataManager.getEntity(clazz));
    }
}
