package site.sorghum.anno.solon.init;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.event.AppBeanLoadEndEvent;
import org.noear.solon.core.event.EventListener;
import org.noear.solon.core.util.ResourceUtil;
import org.noear.solon.core.util.ScanUtil;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._ddl.PlatformFactory;
import site.sorghum.anno._ddl.entity2db.EntityToDdlGenerator;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.pre.plugin.service.impl.AuthServiceImpl;
import site.sorghum.anno._ddl.AnnoEntityToTableGetter;
import site.sorghum.anno._ddl.InitDataService;

import java.net.URL;
import java.util.List;

/**
 * 初始化数据库表结构和预置数据
 *
 * @author songyinyin
 * @since 2023/7/8 11:31
 */
@Slf4j
@Component
public class InitDdlAndDateService implements EventListener<AppBeanLoadEndEvent> {

    @Inject
    AnnoEntityToTableGetter annoEntityToTableGetter;
    @Inject
    InitDataService initDataService;
    @Db
    DbContext dbContext;
    @Inject
    AnnoProperty annoProperty;
    @Inject
    MetadataManager metadataManager;
    @Inject
    AuthServiceImpl authService;

    @Override
    public void onEvent(AppBeanLoadEndEvent appBeanLoadEndEvent) throws Throwable {
        // 维护 entity 对应的表结构
        if (annoProperty.getIsAutoMaintainTable()) {
            PlatformFactory platformFactory = Solon.context().getBean(PlatformFactory.class);
            if (platformFactory == null) {
                platformFactory = new PlatformFactory();
            }
            EntityToDdlGenerator<AnEntity> generator = new EntityToDdlGenerator<>(dbContext, platformFactory, annoEntityToTableGetter);
            List<AnEntity> allEntity = metadataManager.getAllEntity();
            for (AnEntity anEntity : allEntity) {
                if (anEntity.isAutoMaintainTable()) {
                    generator.autoMaintainTable(anEntity);
                }
            }
        }

        // 初始化数据
        List<URL> resources = ScanUtil.scan("init-data", n -> n.endsWith(".sql"))
            .stream()
            .map(ResourceUtil::getResource)
            .toList();
        for (URL resource : resources) {
            try {
                initDataService.init(resource);
            } catch (Exception e) {
                log.error("parse or execute sql error, resource: {}", resource);
                throw e;
            }
        }

        authService.initPermissions();
        authService.initMenus();
    }

}
