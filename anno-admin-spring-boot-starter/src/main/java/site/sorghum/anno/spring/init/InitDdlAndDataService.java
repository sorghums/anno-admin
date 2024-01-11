package site.sorghum.anno.spring.init;

import cn.hutool.core.io.resource.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._ddl.AnnoEntityToTableGetter;
import site.sorghum.anno._ddl.InitDataService;
import site.sorghum.anno._ddl.entity2db.EntityToDdlGenerator;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.plugin.PluginRunner;
import site.sorghum.anno.plugin.service.impl.AuthServiceImpl;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * 初始化数据库表结构和预置数据
 *
 * @author songyinyin
 * @since 2023/7/8 11:31
 */
@Slf4j
@Component
public class InitDdlAndDataService implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    AnnoEntityToTableGetter annoEntityToTableGetter;
    @Autowired
    InitDataService initDataService;
    @Autowired
    DbContext dbContext;
    @Autowired
    AnnoProperty annoProperty;
    @Autowired
    MetadataManager metadataManager;
    @Autowired
    AuthServiceImpl authService;
    @Autowired
    PluginRunner pluginRunner;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        try {
            init();
        } catch (Exception e) {
            throw new BizException(e);
        }
    }

    private void init() throws Exception {
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
            log.warn(e.getMessage());
        }

        for (Resource resource : resources) {
            try {
                initDataService.init(resource.getURL());
            } catch (Exception e) {
                log.error("parse or execute sql error, resource: {}", resource);
                throw e;
            }
        }

        // 初始化anno插件
        pluginRunner.init();

        authService.initPermissions();
        authService.initMenus();

        metadataManager.refresh();
    }

}
