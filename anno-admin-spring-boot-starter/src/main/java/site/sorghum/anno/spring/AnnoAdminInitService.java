package site.sorghum.anno.spring;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.MultiResource;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.noear.wood.WoodConfig;
import org.noear.wood.annotation.Db;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._ddl.AnnoEntityToTableGetter;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.annotation.clazz.AnnoForm;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.global.AnnoScan;
import site.sorghum.anno.anno.util.AnnoUtil;
import site.sorghum.anno.db.service.wood.AnnoWoodConfig;
import site.sorghum.anno.i18n.I18nUtil;
import site.sorghum.anno.method.MethodTemplateManager;
import site.sorghum.anno.method.resource.ResourceFinder;
import site.sorghum.anno.pf4j.Pf4jRunner;
import site.sorghum.anno.plugin.PluginRunner;
import site.sorghum.anno.plugin.service.AnSqlService;
import site.sorghum.anno.plugin.service.impl.AuthServiceImpl;
import site.sorghum.anno.spring.config.AnnoConfig;
import site.sorghum.anno.spring.config.AnnoScanConfig;
import site.sorghum.anno.spring.config.SpringDbConnectionFactory;
import site.sorghum.anno.utils.MTUtils;

import java.util.Locale;
import java.util.Set;

/**
 * 初始化数据库表结构和预置数据
 *
 * @author songyinyin
 * @since 2023/7/8 11:31
 */
@Slf4j
@Component
public class AnnoAdminInitService implements ApplicationListener<ApplicationStartedEvent> {

    @Inject
    AnnoEntityToTableGetter annoEntityToTableGetter;
    @Inject
    AnSqlService sqlService;
    @Db
    DbContext dbContext;
    @Inject
    AnnoProperty annoProperty;
    @Inject
    MetadataManager metadataManager;
    @Inject
    AuthServiceImpl authService;
    @Inject
    PluginRunner pluginRunner;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        try {
            annoInit();
            init();
        } catch (Exception e) {
            throw new BizException(e);
        }
    }

    public void annoInit() throws Exception {
        String[] basePackage = this.getBasePackage(AnnoScanConfig.importingClassMetadata);
        Set<String> packages = CollUtil.newHashSet(basePackage);
        packages.add(AnnoConfig.ANNO_BASE_PACKAGE);

        AnnoBeanUtils.setBean(new SpringBeanImpl());

        // 方法模版初始化
        for (String annoPackage : packages) {
            MethodTemplateManager.parse(annoPackage);
        }
        // Aviator脚本
        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
        instance.useLRUExpressionCache(1000);
        instance.addStaticFunctions("mt", MTUtils.class);
        instance.addInstanceFunctions("s", String.class);

        // 加载 anno 元数据
        loadMetadata(packages);

        MessageSource messageSource = SpringUtil.getBean(MessageSource.class);
        I18nUtil.setI18nService(key -> messageSource.getMessage(key, null, Locale.getDefault()));

        // wood 设置
        WoodConfig.isSelectItemEmptyAsNull = true;
        WoodConfig.isUsingValueNull = true;
        WoodConfig.connectionFactory = new SpringDbConnectionFactory();
        // 初始化wood
        SpringUtil.getBean(AnnoWoodConfig.class).init();

        // pfj support
        new Pf4jRunner();
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
                }
                AnnoForm annoForm = AnnoUtil.getAnnoForm(clazz);
                if (annoForm != null) {
                    metadataManager.loadFormEntity(clazz);
                }

            }
        }
        metadataManager.refresh();
    }

    private String[] getBasePackage(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(AnnoScan.class.getName()));
        if (attributes == null) {
            return new String[]{};
        }
        return attributes.getStringArray("scanPackage");
    }

    private void init() throws Exception {
        metadataManager.refresh();
        // 初始化数据
        MultiResource resources = ResourceFinder.of().find("init-data/**.sql");
        for (Resource resource : resources) {
            sqlService.runResourceSql(resource);
        }
        // 初始化anno插件
        pluginRunner.init();
        authService.initPermissions();
        authService.initMenus();
    }

}
