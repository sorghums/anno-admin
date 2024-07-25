package site.sorghum.anno.solon.metadata;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._ddl.AnnoEntityToTableGetter;
import site.sorghum.anno._ddl.entity2db.EntityToDdlGenerator;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataContext;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.plugin.service.AnSqlService;

import java.util.List;

/**
 * @author songyinyin
 * @since 2023/9/15 10:37
 */
@Component
public class InitDdlAndDataSourceContext implements MetadataContext {
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

    @Override
    public void refresh(List<AnEntity> allEntities) {
        if (annoProperty.getIsAutoMaintainTable()) {
            EntityToDdlGenerator<AnEntity> generator = new EntityToDdlGenerator<AnEntity>(dbContext, annoEntityToTableGetter);
            for (AnEntity anEntity : allEntities) {
                if (anEntity.isAutoMaintainTable() && !anEntity.isVirtualTable()) {
                    generator.autoMaintainTable(anEntity);
                }
            }
        }
    }
}
