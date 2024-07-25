package site.sorghum.anno.spring.metadata;

import jakarta.inject.Inject;
import org.noear.wood.DbContext;
import org.springframework.stereotype.Component;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._ddl.AnnoEntityToTableGetter;
import site.sorghum.anno._ddl.entity2db.EntityToDdlGenerator;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataContext;

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
    DbContext dbContext;
    @Inject
    AnnoProperty annoProperty;

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
