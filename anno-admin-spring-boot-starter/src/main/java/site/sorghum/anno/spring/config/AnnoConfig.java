package site.sorghum.anno.spring.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.extra.spring.SpringUtil;
import jakarta.annotation.PostConstruct;
import org.noear.dami.Dami;
import org.noear.wood.DbContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import site.sorghum.anno._common.AnnoBean;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._ddl.PlatformFactory;
import site.sorghum.anno._metadata.PermissionContext;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.global.AnnoScan;
import site.sorghum.anno.anno.util.AnnoClazzCache;
import site.sorghum.anno.anno.util.AnnoFieldCache;
import site.sorghum.anno.anno.util.AnnoUtil;
import site.sorghum.anno.i18n.I18nUtil;
import site.sorghum.anno.spring.auth.StpInterfaceImpl;

import javax.sql.DataSource;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author songyinyin
 * @since 2023/7/30 12:16
 */
@Configuration
@ComponentScan(basePackages = {AnnoConfig.ANNO_BASE_PACKAGE})
public class AnnoConfig {
    public static final String ANNO_BASE_PACKAGE = "site.sorghum.anno";


    @Bean
    @Primary
    @ConfigurationProperties(prefix = "anno-admin")
    public AnnoProperty annoProperty() {
        return new AnnoProperty();
    }

    @Bean
    public PlatformFactory platformFactory() {
        return new PlatformFactory();
    }

    @Bean
    public DbContext dbContext(DataSource dataSource){
        return new DbContext(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public StpInterfaceImpl stpInterfaceImpl(){
        return new StpInterfaceImpl();
    }


    @PostConstruct
    public void init() {
        String[] basePackage = this.getBasePackage(AnnoScanConfig.importingClassMetadata);
        Set<String> packages = CollUtil.newHashSet(basePackage);
        packages.add(AnnoConfig.ANNO_BASE_PACKAGE);

        Dami.api().registerListener(MetadataManager.METADATA_TOPIC, SpringUtil.getBean(PermissionContext.class));

        // 加载 anno 元数据
        loadMetadata(packages);

        MessageSource messageSource = SpringUtil.getBean(MessageSource.class);
        I18nUtil.setI18nService(key -> messageSource.getMessage(key, null, Locale.getDefault()));

        AnnoBean annoBean = new AnnoBean() {
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
    }


    public void loadMetadata(Set<String> packages) {
        MetadataManager metadataManager = SpringUtil.getBean(MetadataManager.class);

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
                        AnnoFieldCache.putFieldName2FieldAndSql(clazz, columnName, field.getField());
                    }
                );
            }
        }
    }

    private String[] getBasePackage(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(AnnoScan.class.getName()));
        if (attributes == null) {
            return new String[]{};
        }
        return attributes.getStringArray("scanPackage");
    }
}
