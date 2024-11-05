package site.sorghum.anno.om.javacmd;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.SneakyThrows;
import org.noear.liquor.DynamicCompiler;
import site.sorghum.anno._common.entity.CommonParam;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;
import site.sorghum.anno.om.ao.OnlineClassMeta;

import java.util.ArrayList;
import java.util.List;

@Named
public class PreviewClassMetaCmd implements JavaCmdSupplier {

    @Inject
    MetadataManager metadataManager;

    DynamicCompiler dynamicCompiler = new DynamicCompiler(ClassLoader.getSystemClassLoader());


    /**
     * 预查看缓存
     */
    TimedCache<String, Object> preViewCache = CacheUtil.newTimedCache(5 * 60 * 1000);

    @SneakyThrows
    @Override
    public String run(CommonParam param) {
        OnlineClassMeta onlineMeta = param.toT(OnlineClassMeta.class);
        String classContent = onlineMeta.getClassContent();
        List<AnEntity> allEntity = new ArrayList<>();
        Class<?> clazz;
        dynamicCompiler = new DynamicCompiler(ClassLoader.getSystemClassLoader());
        dynamicCompiler.addSource(onlineMeta.getWholeClassName(),classContent).build();
        clazz = dynamicCompiler.getClassLoader().loadClass(onlineMeta.getWholeClassName());
        allEntity.add(metadataManager.loadEntity(clazz, true));
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
