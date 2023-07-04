package site.sorghum.anno.ddl;

import lombok.Getter;

import java.sql.Types;

/**
 * @author songyinyin
 * @since 2023/7/3 14:23
 */
@Getter
public enum TypeEnum {

  ARRAY(Types.ARRAY, "ARRAY"),
  BIGINT(Types.BIGINT, "BIGINT"),
  BINARY(Types.BINARY, "BINARY"),
  BIT(Types.BIT, "BIT"),
  BLOB(Types.BLOB, "BLOB"),
  CHAR(Types.CHAR, "CHAR"),
  CLOB(Types.CLOB, "CLOB"),
  DATE(Types.DATE, "DATE"),
  DECIMAL(Types.DECIMAL, "DECIMAL"),
  DISTINCT(Types.DISTINCT, "DISTINCT"),
  DOUBLE(Types.DOUBLE, "DOUBLE"),
  FLOAT(Types.FLOAT, "FLOAT"),
  INTEGER(Types.INTEGER, "INTEGER"),
  JAVA_OBJECT(Types.JAVA_OBJECT, "JAVA_OBJECT"),
  LONGVARBINARY(Types.LONGVARBINARY, "LONGVARBINARY"),
  LONGVARCHAR(Types.LONGVARCHAR, "LONGVARCHAR"),
  NULL(Types.NULL, "NULL"),
  NUMERIC(Types.NUMERIC, "NUMERIC"),
  OTHER(Types.OTHER, "OTHER"),
  REAL(Types.REAL, "REAL"),
  REF(Types.REF, "REF"),
  SMALLINT(Types.SMALLINT, "SMALLINT"),
  STRUCT(Types.STRUCT, "STRUCT"),
  TIME(Types.TIME, "TIME"),
  TIMESTAMP(Types.TIMESTAMP, "TIMESTAMP"),
  TINYINT(Types.TINYINT, "TINYINT"),
  VARBINARY(Types.VARBINARY, "VARBINARY"),
  VARCHAR(Types.VARCHAR, "VARCHAR"),

  ;
  private final Integer typeCode;

  private final String nativeSqlName;

  TypeEnum(Integer typeCode, String nativeSqlName) {
    this.typeCode = typeCode;
    this.nativeSqlName = nativeSqlName;
  }
}
