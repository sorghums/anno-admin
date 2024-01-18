package site.sorghum.anno.spring.init;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
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
import site.sorghum.anno.plugin.ao.AnSql;
import site.sorghum.anno.plugin.dao.AnSqlDao;
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

    @Inject
    AnnoEntityToTableGetter annoEntityToTableGetter;
    @Inject
    InitDataService initDataService;
    @Db
    AnSqlDao anSqlDao;
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
            log.info("init-data/*.sql not found, please check if the file exists, resource: classpath:init-data/*.sql");
        }

        for (Resource resource : resources) {
            String fileName = resource.getFile().getName().split("/")[resource.getFile().getName().split("/").length - 1];
            AnSql anSql = anSqlDao.queryByVersion(fileName);
            if (anSql == null || anSql.getId() == null){
                anSql = new AnSql(){{
                    setId(IdUtil.getSnowflakeNextIdStr());
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

        metadataManager.refresh();
    }

}
