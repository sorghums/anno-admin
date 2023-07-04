package site.sorghum.anno.ddl;

import cn.hutool.core.util.StrUtil;
import org.noear.wood.wrap.ColumnWrap;
import org.noear.wood.wrap.TableWrap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author songyinyin
 * @since 2023/7/2 16:18
 */
public abstract class DdlGenerator {

  protected DatabaseInfo databaseInfo;

  public DdlGenerator(DatabaseInfo databaseInfo) {
    this.databaseInfo = databaseInfo;
  }

  /**
   * 获取创建表的DDL语句
   *
   * @param tableDefine 表结构定义
   * @return 创建表的DDL语句
   */
  public String getTableDDL(TableWrap tableDefine) {
    StringBuilder sb = new StringBuilder("CREATE TABLE ");
    sb.append(databaseInfo.getDelimiterToken()).append(tableDefine.getName()).append(databaseInfo.getDelimiterToken()).append(" (");

    List<ColumnWrap> columns = tableDefine.getColumns();
    for (ColumnWrap column : columns) {
      sb.append(this.getColumnDefineDDL(column));
      sb.append(",");
    }

    List<String> pks = tableDefine.getPks();
    if (pks != null && !pks.isEmpty()) {
      sb.append(this.getPrimaryKeyDefineDDL(pks)).append(",");
    }

    sb.deleteCharAt(sb.length() - 1);

    if (StrUtil.isNotBlank(tableDefine.getRemarks())) {
      sb.append(") COMMENT='").append(tableDefine.getRemarks()).append("';");
    } else {
      sb.append(");");
    }

    return sb.toString();
  }

  /**
   * 生成字段定义的DDL语句
   *
   * @param column 字段定义
   * @return DDL语句
   */
  public String getColumnDefineDDL(ColumnWrap column) {
    StringBuilder sb = new StringBuilder(databaseInfo.getDelimiterToken() + column.getName() + databaseInfo.getDelimiterToken());

    String nativeType = databaseInfo.getNativeTypes().get(column.getSqlType());
    if (nativeType == null) {
      throw new DdlException("不支持的 jdbc type: " + column.getSqlType());
    }
    sb.append(" ");
    sb.append(nativeType);

    Integer size = column.getSize();
    if (size == null) {
      // 设置字段的默认长度
      size = databaseInfo.getDefaultSize(column.getSqlType());
    }
    if (size != null) {
      sb.append("(").append(size);
      if (column.getDigit() != null) {
        sb.append(",").append(column.getDigit());
      }
      sb.append(")");
    }

    if (StrUtil.isNotBlank(column.getIsNullable())) {
      sb.append(" ").append(column.getIsNullable());
    }

    if (StrUtil.isNotBlank(column.getRemarks())) {
      sb.append(" COMMENT '").append(column.getRemarks()).append("'");
    }
    return sb.toString();
  }

  /**
   * 生成主键定义的DDL语句
   *
   * @param pks 主键字段
   * @return DDL语句
   */
  protected String getPrimaryKeyDefineDDL(List<String> pks) {
    StringBuilder sb = new StringBuilder("PRIMARY KEY(");
    String pkStr = pks.stream().map(columnName -> databaseInfo.getDelimiterToken() + columnName + databaseInfo.getDelimiterToken()).collect(Collectors.joining(","));
    sb.append(pkStr);
    sb.append(") ");
    return sb.toString();
  }

  /**
   * 生成表中不存在的列的DDL
   *
   * @param tableDefine 期望的表结构定义
   * @param existsTable 已存在的表结构
   */
  public List<String> getAddColumnDDL(TableWrap tableDefine, TableWrap existsTable) {
    Set<String> existsColumns = existsTable.getColumns().stream().map(ColumnWrap::getName).collect(Collectors.toSet());
    List<ColumnWrap> addColumns = tableDefine.getColumns().stream().filter(e -> !existsColumns.contains(e.getName())).toList();

    List<String> addColumnDDLs = new ArrayList<>(addColumns.size());
    for (ColumnWrap addColumn : addColumns) {
      StringBuilder sb = new StringBuilder("ALTER TABLE ");
      sb.append(databaseInfo.getDelimiterToken()).append(tableDefine.getName()).append(databaseInfo.getDelimiterToken()).append(" ADD COLUMN ");
      sb.append(getColumnDefineDDL(addColumn)).append(databaseInfo.getSqlCommandDelimiter());
      addColumnDDLs.add(sb.toString());
    }
    return addColumnDDLs;
  }

  /**
   * 表已存在，比较两个表不同的列，并生成列变化的DDL
   *
   * @param sourceTableDefine 源表
   * @param targetTableDefine 目标表
   */
  protected List<String> getModifiedColumnDDL(TableWrap sourceTableDefine, TableWrap targetTableDefine) {
    return null;
  }

}
