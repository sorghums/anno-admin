package site.sorghum.anno.anno.controller;

import cn.hutool.core.lang.Singleton;
import jakarta.inject.Inject;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.datasupplier.AnnoDynamicFormAndDataSupplier;

import java.util.List;
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
            return AnnoResult.succeed(
                Singleton.get(
                    entity.getEntityName(),() -> JSONUtil.toBean(entity,Map.class)
                )
            );
        }
        return AnnoResult.succeed();
    }

    /**
     * 默认添加数据
     *
     * @param entityName    实体名称
     * @param columnIdList 列id列表
     * @return {@link Map }<{@link String },{@link Object }>
     */
    public Map<String,Object> defaultAddData(String entityName, List<String> columnIdList){
        return AnnoDynamicFormAndDataSupplier.getInstance(entityName).execute(entityName, columnIdList);
    }
}
