package site.sorghum.anno.modular.ddl;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.event.AppLoadEndEvent;
import org.noear.solon.core.event.EventListener;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.common.config.AnnoProperty;
import site.sorghum.anno.ddl.entity2db.EntityToDdlGenerator;
import site.sorghum.anno.modular.anno.util.AnnoClazzCache;

import java.util.Collection;

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

  @Override
  public void onEvent(AppLoadEndEvent appLoadEndEvent) throws Throwable {
    // 维护 entity 对应的表结构
    if (annoProperty.getIsAutoMaintainTable()) {
      EntityToDdlGenerator generator = new EntityToDdlGenerator(dbContext, annoEntityToTableGetter);
      Collection<Class<?>> classes = AnnoClazzCache.fetchAllClazz();
      for (Class<?> clazz : classes) {
        generator.autoMaintainTable(clazz);
      }
    }

    // 初始化数据
    initDataService.init();
  }

}
