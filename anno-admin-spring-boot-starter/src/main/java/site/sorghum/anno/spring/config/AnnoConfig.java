package site.sorghum.anno.spring.config;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.extra.spring.SpringUtil;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import site.sorghum.anno._common.AnnoBean;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.global.AnnoScan;
import site.sorghum.anno.anno.util.AnnoClazzCache;
import site.sorghum.anno.anno.util.AnnoFieldCache;
import site.sorghum.anno.anno.util.AnnoUtil;
import site.sorghum.anno.i18n.I18nService;
import site.sorghum.anno.i18n.I18nUtil;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author songyinyin
 * @since 2023/7/30 12:16
 */
@Configuration
public class AnnoConfig {
    private static final String ANNO_BASE_PACKAGE = "site.sorghum.anno";

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "anno-admin")
    public AnnoProperty annoProperty() {
        return new AnnoProperty();
    }


    @PostConstruct
    public AnnoBean annoBean(){
        AnnoBean annoBean =  new AnnoBean(){
            @Override
            public <T> T getBean(String name) {
                return SpringUtil.getBean(name);
            }

            @Override
            public <T> T getBean(Class<T> type) {
                return SpringUtil.getBean(type);
            }

            @Override
            public <T> List<T> getBeansOfType(Class<T> baseType) {
                return SpringUtil.getBeansOfType(baseType).values().stream().toList();
            }
        };
        AnnoBeanUtils.setBean(annoBean);
        Set<String> packages = CollUtil.newHashSet(ANNO_BASE_PACKAGE);
//        AnnoScan annotation = AnnotationUtil.getAnnotation(source, AnnoScan.class);
//        if (Objects.nonNull(annotation)) {
//            String[] value = annotation.scanPackage();
//            if (value.length > 0) {
//                packages.addAll(CollUtil.newArrayList(value));
//            }
//        }
        // 加载 anno 元数据
        loadMetadata(packages);

        I18nUtil.setI18nService(new I18nService() {
            @Override
            public String getMessage(String key) {
                return key;
            }
        });
        return annoBean;
    }

    public void loadMetadata(Set<String> packages){
        MetadataManager metadataManager =  SpringUtil.getBean(MetadataManager.class);

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
                            AnnoFieldCache.putFieldName2FieldAndSql(clazz, columnName, field);
                        }
                );
            }
        }
    }
}
