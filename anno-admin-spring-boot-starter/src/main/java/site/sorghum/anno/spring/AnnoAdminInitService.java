package site.sorghum.anno.spring;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._ddl.AnnoEntityToTableGetter;
import site.sorghum.anno._ddl.InitDataService;
import site.sorghum.anno._ddl.entity2db.EntityToDdlGenerator;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.global.AnnoScan;
import site.sorghum.anno.anno.util.AnnoClazzCache;
import site.sorghum.anno.anno.util.AnnoFieldCache;
import site.sorghum.anno.i18n.I18nUtil;
import site.sorghum.anno.method.MethodTemplateManager;
import site.sorghum.anno.plugin.PluginRunner;
import site.sorghum.anno.plugin.ao.AnSql;
import site.sorghum.anno.plugin.dao.AnSqlDao;
import site.sorghum.anno.plugin.service.impl.AuthServiceImpl;
import site.sorghum.anno.spring.config.AnnoConfig;
import site.sorghum.anno.spring.config.AnnoScanConfig;
import site.sorghum.anno.utils.MTUtils;

import java.io.FileNotFoundException;
import java.util.List;
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
    InitDataService initDataService;
    @Db
    DbContext dbContext;
    @Db
    AnSqlDao anSqlDao;
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


    }


    public void loadMetadata(Set<String> packages) {
        MetadataManager metadataManager = SpringUtil.getBean(MetadataManager.class);

        for (String scanPackage : packages) {
            Set<Class<?>> classes = ClassUtil.scanPackage(scanPackage);
            for (Class<?> clazz : classes) {
                if (clazz.isInterface()) {
                    continue;
                }
                AnnoMain annoMain = AnnotationUtil.getAnnotation(clazz, AnnoMain.class);
                if (annoMain != null) {
                    AnEntity anEntity = metadataManager.loadEntity(clazz);
                    // 缓存处理类
                    AnnoClazzCache.put(clazz.getSimpleName(), clazz);
                    // 缓存字段信息
                    for (AnField field : anEntity.getFields()) {
                        String columnName = field.getTableFieldName();
                        AnnoFieldCache.putFieldName2FieldAndSql(clazz, columnName, field.getFieldName());
                        // 同时保存其实际节点的类的字段信息
                        if (clazz != field.getDeclaringClass()) {
                            AnnoFieldCache.putFieldName2FieldAndSql(field.getDeclaringClass(), columnName, field.getFieldName());
                        }
                    }
                }
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

    private void init() throws Exception {
        metadataManager.refresh();

        // 维护 entity 对应的表结构
        if (annoProperty.getIsAutoMaintainTable()) {
            EntityToDdlGenerator<AnEntity> generator = new EntityToDdlGenerator<>(dbContext, annoEntityToTableGetter);
            List<AnEntity> allEntity = metadataManager.getAllEntity();
            for (AnEntity anEntity : allEntity) {
                if (anEntity.isAutoMaintainTable()) {
                    generator.autoMaintainTable(anEntity);
                }
            }
        }

        // 初始化数据
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = {};
        try {
            resources = resolver.getResources("classpath:init-data/*.sql");
        }catch (FileNotFoundException e){
            log.info("init-data/*.sql not found, please check if the file exists, resource: classpath:init-data/*.sql");
        }

        for (Resource resource : resources) {
            String fileName = resource.getFile().getName().split("/")[resource.getFile().getName().split("/").length - 1];
            AnSql anSql = anSqlDao.queryByVersion(fileName);
            if (anSql == null || anSql.getId() == null){
                anSql = new AnSql(){{
                    setVersion(fileName);
                    setState(0);
                }};
            }
            if (annoProperty.getIsAutoMaintainInitData() && anSql.getState() != 1){
                try {
                    initDataService.init(resource.getURL());
                    anSql.setState(1);
                } catch (Exception e) {
                    anSql.setState(2);
                    log.error("parse or execute sql error, resource: {}", resource);
                    throw e;
                } finally {
                    anSql.setRunTime(DateUtil.date());
                    anSqlDao.saveOrUpdate(anSql);
                }
            }else {
                anSqlDao.saveOrUpdate(anSql);
            }
        }

        // 初始化anno插件
        pluginRunner.init();

        authService.initPermissions();
        authService.initMenus();
    }

}
