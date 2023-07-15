package site.sorghum.anno.modular.ddl;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.event.AppLoadEndEvent;
import org.noear.solon.core.event.EventListener;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.common.config.AnnoProperty;
import site.sorghum.anno.ddl.entity2db.EntityToDdlGenerator;
import site.sorghum.anno.metadata.AnEntity;
import site.sorghum.anno.metadata.MetadataManager;

import java.util.List;

/**
 * 初始化数据库表结构和预置数据
 *
 * @author songyinyin
 * @since 2023/7/8 11:31
 */
@Component
public class InitDdlAndDateService implements EventListener<AppLoadEndEvent> {

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

    @Override
    public void onEvent(AppLoadEndEvent appLoadEndEvent) throws Throwable {
        // 维护 entity 对应的表结构
        if (annoProperty.getIsAutoMaintainTable()) {
            EntityToDdlGenerator generator = new EntityToDdlGenerator(dbContext, annoEntityToTableGetter);
            List<AnEntity> allEntity = metadataManager.getAllEntity();
            for (AnEntity anEntity : allEntity) {
                if (anEntity.isAutoMaintainTable()) {
                    generator.autoMaintainTable(anEntity.getClazz());
                }
            }
        }

        // 初始化数据
        initDataService.init();
    }

}
