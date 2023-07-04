package site.sorghum.anno.ddl;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.solon.test.SolonTest;
import org.noear.wood.DbContext;
import org.noear.wood.DbContextMetaData;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.ddl.app.TestApp;
import site.sorghum.anno.ddl.app.entity.TestEntity;
import site.sorghum.anno.ddl.entity2db.EntityToDdlGenerator;

/**
 * @author songyinyin
 * @since 2023/7/4 11:50
 */
@Slf4j
@SolonTest(TestApp.class)
@RunWith(SolonJUnit4ClassRunner.class)
public class EntityToDdlGeneratorTest {


  @Db
  DbContext dbContext;

  @Test
  public void testCreateTableDDL() throws Exception {
    dbContext.exe("DROP TABLE test_entity");
    EntityToDdlGenerator generator = new EntityToDdlGenerator(dbContext);
    String tableDDL = generator.getCreateTableDDL(TestEntity.class);
    log.info("ddl==>{}", tableDDL);

    generator.executeCreateTableDDL(TestEntity.class);
    dbContext.getMetaData().refresh();
    Assert.assertNotNull(dbContext.getMetaData().getTable("test_entity"));
  }

  @Test
  public void testAddColumns() throws Exception {
    dbContext.exe("DROP TABLE test_entity");
    dbContext.exe("CREATE TABLE `test_entity` (`id` VARCHAR(254) NOT NULL,`create_time` DATETIME DEFAULT NULL,`big_decimal_num` DECIMAL(25,6) NOT NULL DEFAULT 0,`float_num` DOUBLE DEFAULT NULL,`double_num` DOUBLE DEFAULT NULL,`integer_num` INTEGER DEFAULT NULL,`long_num` BIGINT DEFAULT NULL,`util_date` DATETIME DEFAULT NULL,`sql_date` DATETIME DEFAULT NULL,PRIMARY KEY(`id`) );");

    dbContext.getMetaData().refresh();
    Assert.assertEquals(9, dbContext.getMetaData().getTable("test_entity").getColumns().size());

    EntityToDdlGenerator generator = new EntityToDdlGenerator(dbContext);
    generator.executeTableAddedColumnDDL(TestEntity.class);
    dbContext.getMetaData().refresh();
    DbContextMetaData metaData = dbContext.getMetaData();

    Assert.assertEquals(10, metaData.getTable("test_entity").getColumns().size());
  }
}
