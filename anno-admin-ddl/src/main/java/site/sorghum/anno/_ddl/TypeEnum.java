package site.sorghum.anno._ddl;

import com.github.drinkjava2.jdialects.Type;
import lombok.Getter;
import site.sorghum.anno._ddl.jdialects.JdiDataBaseInfo;

import java.sql.Types;

/**
 * @author songyinyin
 * @since 2023/7/3 14:23
 */
@Getter
public enum TypeEnum {
  ARRAY(Types.ARRAY, Type.JAVA_OBJECT),
  BIGINT(Types.BIGINT, Type.BIGINT),
  BINARY(Types.BINARY, Type.BINARY),
  BIT(Types.BIT, Type.BIT),
  BLOB(Types.BLOB, Type.BLOB),
  CHAR(Types.CHAR, Type.CHAR),
  CLOB(Types.CLOB, Type.CLOB),
  DATE(Types.DATE, Type.DATE),
  DECIMAL(Types.DECIMAL, Type.DECIMAL),
  DOUBLE(Types.DOUBLE, Type.DOUBLE),
  FLOAT(Types.FLOAT, Type.FLOAT),
  INTEGER(Types.INTEGER, Type.INTEGER),
  JAVA_OBJECT(Types.JAVA_OBJECT, Type.JAVA_OBJECT),
  LONGVARBINARY(Types.LONGVARBINARY, Type.LONGVARBINARY),
  LONGVARCHAR(Types.LONGVARCHAR, Type.LONGVARCHAR),
  NUMERIC(Types.NUMERIC, Type.NUMERIC),
  REAL(Types.REAL, Type.REAL),
  SMALLINT(Types.SMALLINT, Type.SMALLINT),
  STRUCT(Types.STRUCT, Type.JSON),
  TIME(Types.TIME, Type.TIME),
  TIMESTAMP(Types.TIMESTAMP, Type.TIMESTAMP),
  TINYINT(Types.TINYINT, Type.TINYINT),
  VARBINARY(Types.VARBINARY, Type.VARBINARY),
  VARCHAR(Types.VARCHAR, Type.VARCHAR),

  ;
  private final Integer typeCode;

  private final Type nativeType;

  TypeEnum(Integer typeCode, Type nativeType) {
    this.typeCode = typeCode;
    this.nativeType = nativeType;
  }

  public static Type getType(Integer typeCode) {
    for (TypeEnum typeEnum : TypeEnum.values()) {
      if (typeEnum.typeCode.equals(typeCode)) {
        return typeEnum.getNativeType();
      }
    }
    return JdiDataBaseInfo.getNativeType(typeCode);
  }
}
