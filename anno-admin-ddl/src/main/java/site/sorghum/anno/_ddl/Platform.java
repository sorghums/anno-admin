package site.sorghum.anno._ddl;

import lombok.Getter;
import org.noear.wood.DbContextMetaData;

/**
 * 平台封装与数据库相关的功能，比如 ddl 生成器，以及数据库的类型映射
 *
 * @author songyinyin
 * @since 2023/7/2 17:53
 */
public abstract class Platform {


  @Getter
  protected DdlGenerator ddlGenerator;

  @Getter
  protected DatabaseInfo databaseInfo;

  public Platform(DbContextMetaData dbContextMetaData) {
    this.databaseInfo = new DatabaseInfo(dbContextMetaData);
  }

  protected abstract boolean isSupport();

}
