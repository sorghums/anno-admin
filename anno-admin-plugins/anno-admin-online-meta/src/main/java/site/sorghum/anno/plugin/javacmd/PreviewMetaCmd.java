package site.sorghum.anno.plugin.javacmd;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import net.bytebuddy.description.type.TypeDescription;
import site.sorghum.anno._common.entity.CommonParam;
import site.sorghum.anno._common.util.MetaClassUtil;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;
import site.sorghum.anno.plugin.ao.OnlineMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
public class PreviewMetaCmd implements JavaCmdSupplier {

    @Inject
    MetadataManager metadataManager;

    /**
     * 预查看缓存
     */
    TimedCache<String, Object> preViewCache = CacheUtil.newTimedCache(5 * 60 * 1000);

    @Override
    public String run(CommonParam param) {
        OnlineMeta onlineMeta = param.toT(OnlineMeta.class);
        String ymlContent = onlineMeta.getYmlContent();
        List<AnEntity> allEntity = new ArrayList<>();
        Map<TypeDescription, Class<?>> typeDescriptionClassMap = MetaClassUtil.loadClass(ymlContent);
        typeDescriptionClassMap.values().forEach(
            c -> allEntity.add(metadataManager.loadEntity(c, true))
        );
        StringBuilder pathParam = new StringBuilder();
        // 增加预览缓存
        for (AnEntity anEntity : allEntity) {
            preViewCache.put(anEntity.getEntityName(), true);
            pathParam.append(anEntity.getEntityName()).append(",");
        }
        // 去除
        pathParam.deleteCharAt(pathParam.length() - 1);
        metadataManager.refresh();
        return "iframe://#/basic-preview?entityNames=%s".formatted(pathParam);
    }
}
