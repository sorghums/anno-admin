package site.sorghum.anno.solon.metadata;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._ddl.AnnoEntityToTableGetter;
import site.sorghum.anno._ddl.entity2db.EntityToDdlGenerator;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataContext;
import site.sorghum.anno.db.service.context.AnnoDbContext;

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
    AnnoProperty annoProperty;

    @Override
    public void refresh(List<AnEntity> allEntities) {
        if (annoProperty.getIsAutoMaintainTable()) {
            for (AnEntity anEntity : allEntities) {
                if (anEntity.isAutoMaintainTable() && !anEntity.isVirtualTable()) {
                    AnnoDbContext.dynamicDbContext(
                        anEntity.getDbName(),
                        () -> {
                            EntityToDdlGenerator<AnEntity> generator = EntityToDdlGenerator.of(AnnoDbContext.dbContext(), annoEntityToTableGetter);
                            generator.autoMaintainTable(anEntity);
                            return Boolean.TRUE;
                        }
                    );
                }
            }
        }
    }
}
