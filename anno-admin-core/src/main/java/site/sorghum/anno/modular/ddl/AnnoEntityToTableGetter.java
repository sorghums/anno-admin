package site.sorghum.anno.modular.ddl;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import org.noear.solon.annotation.Component;
import org.noear.wood.DbContext;
import org.noear.wood.annotation.Db;
import org.noear.wood.annotation.PrimaryKey;
import org.noear.wood.annotation.Table;
import org.noear.wood.wrap.ColumnWrap;
import org.noear.wood.wrap.TableWrap;
import site.sorghum.anno.ddl.entity2db.SampleEntityToTableGetter;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.util.AnnoUtil;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.Collections;
import java.util.List;

/**
 * anno 的实体类，转成 table
 *
 * @author songyinyin
 * @since 2023/7/4 22:44
 */
@Component
public class AnnoEntityToTableGetter extends SampleEntityToTableGetter {

  @Override
  public TableWrap getTable(Class<?> clazz) {

    AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
    Table table = AnnotationUtil.getAnnotation(clazz, Table.class);
    String tableName = null;
    if (table != null) {
      tableName = table.value();
    }
    if (StrUtil.isBlank(tableName)) {
      // 默认使用小写，下划线命名
      tableName = StrUtil.toUnderlineCase(clazz.getSimpleName());
    }

    TableWrap tableWrap = new TableWrap(tableName, annoMain.name());

    List<Field> declaredFields = AnnoUtil.getAnnoFields(clazz);
    // 将 id 字段放到第一位
    Field idField = CollUtil.findOne(declaredFields, e -> e.getName().equals("id"));
    ListUtil.swapTo(declaredFields, idField, 0);

    for (Field field : declaredFields) {
      // 不是基本类型，跳过
      if (!columnIsSupport(field)) {
        continue;
      }
      AnnoField annoField = field.getAnnotation(AnnoField.class);
      String columnName = annoField.tableFieldName();
      if (StrUtil.isBlank(columnName)) {
        // 默认使用小写，下划线命名
        columnName = StrUtil.toUnderlineCase(field.getName());
      }

      int sqlType;
      Integer size = null;
      Integer digit = null;
      String defaultValue = "DEFAULT NULL";

      switch (annoField.dataType()) {
        case STRING -> {
          sqlType = Types.VARCHAR;
          size = 254;
        }
        case TREE, OPTIONS -> {
          // TREE 和 OPTIONS，通过 anno 注解获取不到字段类型，需要通过字段类型获取
          ColumnWrap column = getColumn(clazz, field);
          sqlType = column.getSqlType();
          size = column.getSize();
          digit = column.getDigit();
        }
        case DATE -> sqlType = Types.DATE;
        case DATETIME -> sqlType = Types.TIMESTAMP;
        case NUMBER -> {
          sqlType = Types.NUMERIC;
          size = 25;
          digit = 6;
          defaultValue = "NOT NULL DEFAULT 0";
        }
        case RICH_TEXT, EDITOR -> sqlType = Types.CLOB;
        default -> { // FILE, IMAGE and others
          sqlType = Types.VARCHAR;
          size = 512;
        }
      }

      if (AnnotationUtil.getAnnotation(field, PrimaryKey.class) != null) {
        defaultValue = "NOT NULL";
        tableWrap.addPk(columnName);
        size = 32;
      }

      if (annoField.fieldSize() != 0) {
        size = annoField.fieldSize();
      }

      if (StrUtil.isNotBlank(annoField.defaultValue())) {
        defaultValue = annoField.defaultValue();
      }

      ColumnWrap columnWrap = new ColumnWrap(columnName, sqlType, size, digit, defaultValue, annoField.title());
      tableWrap.addColumn(columnWrap);
    }

    return tableWrap;
  }

}
