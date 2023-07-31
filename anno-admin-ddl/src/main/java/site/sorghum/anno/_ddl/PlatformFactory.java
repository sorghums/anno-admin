package site.sorghum.anno._ddl;

import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContextMetaData;
import org.noear.wood.wrap.DbType;
import site.sorghum.anno._ddl.dialect.MysqlPlatform;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author songyinyin
 * @since 2023/7/2 15:52
 */
@Slf4j
public class PlatformFactory {

  /**
   * key：数据库名称，value：
   */
  private List<Class<? extends Platform>> platforms = new ArrayList<>();

  public PlatformFactory() {
    this.registerDefaultDatabasePlatform();
  }

  public PlatformFactory(List<Class<? extends Platform>> platforms) {
    this.platforms = platforms;
  }

  /**
   * 获取对应数据库的 ddl 生成器
   *
   * @param dbContextMetaData 数据库元信息
   */
  public Platform getPlatformInstance(DbContextMetaData dbContextMetaData) {
    for (Class<? extends Platform> platformClass : platforms) {
      try {
        Platform platform = platformClass.getDeclaredConstructor(DbContextMetaData.class).newInstance(dbContextMetaData);
        if (platform.isSupport()) {
          return platform;
        }
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        log.warn("init platform error, continue", e);
      }
    }
    DbType type = dbContextMetaData.getType();
    throw new DdlException("不支持的数据库类型: " + type.name());
  }

  public void registerDefaultDatabasePlatform() {
    platforms.add(MysqlPlatform.class);
  }

  public void registerPlatform(Class<? extends Platform> platformClass) {
    platforms.add(platformClass);
  }
}
