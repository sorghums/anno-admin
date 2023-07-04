package site.sorghum.anno.ddl.entity2db;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Setter;
import org.noear.solon.core.util.ReflectUtil;
import org.noear.wood.utils.NamingUtils;
import org.noear.wood.wrap.ColumnWrap;
import org.noear.wood.wrap.TableWrap;
import site.sorghum.anno.ddl.DdlException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author songyinyin
 * @since 2023/7/3 22:48
 */
public class SampleEntityToTableGetter implements EntityToTableGetter {

  @Setter
  private String defaultPkName = "id";


  @Override
  public TableWrap getTable(Class<?> clazz) {
    String simpleName = clazz.getSimpleName();
    String tableName = NamingUtils.toUnderlineString(simpleName);
    TableWrap tableWrap = new TableWrap(tableName, null);

    Field[] declaredFields = ReflectUtil.getDeclaredFields(clazz);
    for (Field field : declaredFields) {
      // 默认主键
      if (StrUtil.isNotBlank(defaultPkName) && defaultPkName.equals(field.getName())) {
        tableWrap.addPk(field.getName());
      }
      // 基本类型
      if (columnIsSupport(field)) {
        ColumnWrap column = getColumn(clazz, field);
        if (column != null) {
          tableWrap.addColumn(column);
        }
      }
    }
    return tableWrap;
  }

  @Override
  public ColumnWrap getColumn(Class<?> clazz, Field field) {
    String fieldName = NamingUtils.toUnderlineString(field.getName());
    Class<?> fieldType = field.getType();
    Integer sqlType;
    Integer size = null;
    Integer digit = null;
    String defaultValue = "DEFAULT NULL";
    if (fieldName.equals(defaultPkName)) {
      defaultValue = "NOT NULL";
    }
    if (fieldType == String.class) {
      sqlType = Types.VARCHAR;
      size = 254;
    } else if (fieldType == LocalDate.class) {
      sqlType = Types.DATE;
    } else if (fieldType == LocalDateTime.class || fieldType == java.util.Date.class || fieldType == java.sql.Date.class) {
      sqlType = Types.TIMESTAMP;
    } else if (fieldType == Integer.class) {
      sqlType = Types.INTEGER;
    } else if (fieldType == Long.class) {
      sqlType = Types.BIGINT;
    } else if (fieldType == Float.class) {
      sqlType = Types.FLOAT;
    } else if (fieldType == Double.class) {
      sqlType = Types.DOUBLE;
    } else if (fieldType == BigDecimal.class) {
      sqlType = Types.NUMERIC;
      size = 25;
      digit = 6;
      defaultValue = "NOT NULL DEFAULT 0";
    } else {
      throw new DdlException("%s#%s 不支持的字段类型：%s".formatted(clazz.getName(), field.getName(), fieldType.getSimpleName()));
    }

    return new ColumnWrap(fieldName, sqlType, size, digit, defaultValue, null);
  }

  public boolean columnIsSupport(Field field) {
    Class<?> fieldType = field.getType();

    return ClassUtil.isBasicType(field.getType())
        || CharSequence.class.isAssignableFrom(fieldType)
        || Number.class.isAssignableFrom(fieldType)
        || Date.class.isAssignableFrom(fieldType)
        || java.sql.Date.class.isAssignableFrom(fieldType)
        || LocalDate.class.isAssignableFrom(fieldType)
        || LocalDateTime.class.isAssignableFrom(fieldType);
  }
}
