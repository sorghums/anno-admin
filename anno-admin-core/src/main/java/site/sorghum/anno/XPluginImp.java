package site.sorghum.anno;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import org.noear.solon.Solon;
import org.noear.solon.annotation.ProxyComponent;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.Plugin;
import site.sorghum.anno.metadata.MetadataManager;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.global.AnnoScan;
import site.sorghum.anno.modular.anno.util.AnnoClazzCache;
import site.sorghum.anno.modular.anno.util.AnnoFieldCache;
import site.sorghum.anno.modular.anno.util.AnnoUtil;

import java.util.Objects;
import java.util.Set;

/**
 * Solon Anno-Admin 插件
 *
 * @author sorghum
 * @since 2023/05/20
 */
@ProxyComponent
public class XPluginImp implements Plugin {
    private static final String ANNO_BASE_PACKAGE = "site.sorghum.anno";

    @Override
    public void start(AopContext context) {
        MetadataManager metadataManager = new MetadataManager();
        context.wrapAndPut(MetadataManager.class, metadataManager);

        // 扫描包
        context.beanScan(ANNO_BASE_PACKAGE);
        Class<?> source = Solon.app().source();
        Set<String> packages = CollUtil.newHashSet(source.getPackage().getName(), ANNO_BASE_PACKAGE);
        AnnoScan annotation = AnnotationUtil.getAnnotation(source, AnnoScan.class);
        if (Objects.nonNull(annotation)) {
            String[] value = annotation.scanPackage();
            if (value.length > 0) {
                packages.addAll(CollUtil.newArrayList(value));
            }
        }

        context.beanInject(metadataManager);

        for (String scanPackage : packages) {
            Set<Class<?>> classes = ClassUtil.scanPackage(scanPackage);
            for (Class<?> clazz : classes) {
                if (clazz.isInterface()) {
                    continue;
                }
                AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
                if (annoMain != null) {

                    metadataManager.loadEntity(clazz);
                    // 缓存处理类
                    AnnoClazzCache.put(clazz.getSimpleName(), clazz);
                }
                // 缓存字段信息
                AnnoUtil.getAnnoFields(clazz).forEach(
                        field -> {
                            String columnName = AnnoUtil.getColumnName(field);
                            AnnoFieldCache.putFieldName2FieldAndSql(clazz,columnName,field);
                        }
                );
            }
        }

    }

}
