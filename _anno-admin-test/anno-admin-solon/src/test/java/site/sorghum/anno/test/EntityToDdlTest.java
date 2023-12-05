package site.sorghum.anno.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.noear.wood.DbContext;
import org.noear.wood.DbContextMetaData;
import org.noear.wood.annotation.Db;
import org.noear.wood.wrap.ColumnWrap;
import org.noear.wood.wrap.TableWrap;
import site.sorghum.anno.test.base.BaseAppTest;

/**
 * @author songyinyin
 * @since 2023/7/4 11:50
 */
@Slf4j
public class EntityToDdlTest extends BaseAppTest {


  @Db
  DbContext dbContext;

  @Test
  public void testBaseEntity() throws Exception {
    DbContextMetaData contextMetaData = dbContext.getMetaData();
    contextMetaData.refresh();

    TableWrap table = contextMetaData.getTable("sys_user");
    ColumnWrap idColumn = getColumn(table, "id");
    Assert.assertEquals(32, idColumn.getSize().intValue());

    // create_by
    ColumnWrap createByColumn = getColumn(table, "create_by");
    Assert.assertEquals(32, createByColumn.getSize().intValue());

    // update_by
    ColumnWrap updateByColumn = getColumn(table, "update_by");
    Assert.assertEquals(32, updateByColumn.getSize().intValue());
  }

  private ColumnWrap getColumn(TableWrap table, String columnName) {
    return table.getColumns().stream().filter(column -> column.getName().equals(columnName)).findFirst().orElse(null);
  }

}
