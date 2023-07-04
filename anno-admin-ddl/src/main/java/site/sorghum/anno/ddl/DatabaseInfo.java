package site.sorghum.anno.ddl;

import lombok.Data;
import org.noear.wood.DbContextMetaData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author songyinyin
 * @since 2023/7/2 21:17
 */
@Data
public class DatabaseInfo {

  /**
   * 数据库元信息
   */
  protected DbContextMetaData dbContextMetaData;

  /**
   * key: typeCode The {@link java.sql.Types} type code
   * value: 数据库的类型
   */
  private final Map<Integer, String> nativeTypes = new HashMap<>();

  /**
   * Contains the default sizes for those JDBC types whose corresponding native types require a size.
   */
  private Map<Integer, Integer> typesDefaultSizes = new HashMap<>();

  /**
   * The string used for delimiting SQL identifiers, e.g. table names, column names etc.
   */
  private String delimiterToken = "\"";

  /**
   * The text separating individual sql commands.
   */
  private String sqlCommandDelimiter = ";";

  public DatabaseInfo(DbContextMetaData dbContextMetaData) {
    this.dbContextMetaData = dbContextMetaData;
    for (TypeEnum value : TypeEnum.values()) {
      nativeTypes.put(value.getTypeCode(), value.getNativeSqlName());
    }
  }

  public void addNativeTypeMapping(Integer jdbcTypeCode, String nativeType) {
    nativeTypes.put(jdbcTypeCode, nativeType);
  }

  /**
   * Adds a default size for the given jdbc type.
   *
   * @param jdbcTypeCode The jdbc type code
   * @param defaultSize  The default size
   */
  public void setDefaultSize(int jdbcTypeCode, int defaultSize) {
    this.typesDefaultSizes.put(jdbcTypeCode, defaultSize);
  }

  /**
   * Returns the default size value for the given type, if any.
   *
   * @param jdbcTypeCode The jdbc type code
   * @return The default size or <code>null</code> if none is defined
   */
  public Integer getDefaultSize(int jdbcTypeCode) {
    return typesDefaultSizes.get(jdbcTypeCode);
  }
}
