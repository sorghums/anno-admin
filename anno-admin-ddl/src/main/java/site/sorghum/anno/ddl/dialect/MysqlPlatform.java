package site.sorghum.anno.ddl.dialect;

import org.noear.wood.DbContextMetaData;
import org.noear.wood.wrap.DbType;
import site.sorghum.anno.ddl.Platform;

import java.sql.Types;

/**
 * @author songyinyin
 * @since 2023/7/2 17:57
 */
public class MysqlPlatform extends Platform {


  public MysqlPlatform(DbContextMetaData dbContextMetaData) {
    super(dbContextMetaData);

    databaseInfo.setDelimiterToken("`");

    databaseInfo.addNativeTypeMapping(Types.ARRAY, "LONGBLOB");
    databaseInfo.addNativeTypeMapping(Types.BIT, "BIT");
    databaseInfo.addNativeTypeMapping(Types.BLOB, "LONGBLOB");
    databaseInfo.addNativeTypeMapping(Types.CLOB, "LONGTEXT");
    databaseInfo.addNativeTypeMapping(Types.DISTINCT, "LONGBLOB");
    databaseInfo.addNativeTypeMapping(Types.FLOAT, "DOUBLE");
    databaseInfo.addNativeTypeMapping(Types.JAVA_OBJECT, "LONGBLOB");
    databaseInfo.addNativeTypeMapping(Types.LONGVARBINARY, "MEDIUMBLOB");
    databaseInfo.addNativeTypeMapping(Types.LONGVARCHAR, "MEDIUMTEXT");
    databaseInfo.addNativeTypeMapping(Types.NULL, "MEDIUMBLOB");
    databaseInfo.addNativeTypeMapping(Types.NUMERIC, "DECIMAL");
    databaseInfo.addNativeTypeMapping(Types.OTHER, "LONGBLOB");
    databaseInfo.addNativeTypeMapping(Types.REAL, "FLOAT");
    databaseInfo.addNativeTypeMapping(Types.REF, "MEDIUMBLOB");
    databaseInfo.addNativeTypeMapping(Types.STRUCT, "LONGBLOB");
    // Since TIMESTAMP is not a stable datatype yet, and does not support a
    // higher precision
    // than DATETIME (year to seconds) as of MySQL 5, we map the JDBC type
    // here to DATETIME
    databaseInfo.addNativeTypeMapping(Types.TIMESTAMP, "DATETIME");
    // In MySql, TINYINT has only a range of -128 to 127
    databaseInfo.addNativeTypeMapping(Types.TINYINT, "SMALLINT");

    databaseInfo.setDefaultSize(Types.CHAR, 254);
    databaseInfo.setDefaultSize(Types.VARCHAR, 254);
    databaseInfo.setDefaultSize(Types.BINARY, 254);
    databaseInfo.setDefaultSize(Types.VARBINARY, 254);

    this.ddlGenerator = new MysqlDdlGenerator(databaseInfo);
  }

  @Override
  protected boolean isSupport() {
    return DbType.MySQL.equals(databaseInfo.getDbContextMetaData().getType());
  }
}
