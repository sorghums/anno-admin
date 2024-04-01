package site.sorghum.anno.anno.controller;

import jakarta.inject.Inject;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;

import java.util.Map;

/**
 * 功能控制器
 *
 * @author Sorghum
 * @since 2023/05/19
 */
public class AnEntityBaseController {

    @Inject
    protected MetadataManager metadataManager;

    public AnnoResult<Map<Object,Object>> anEntity(String clazz){
        AnEntity entity = metadataManager.getEntity(clazz);
        if (entity != null){
            return AnnoResult.succeed(JSONUtil.toBean(entity,Map.class));
        }
        return AnnoResult.succeed();
    }
}
