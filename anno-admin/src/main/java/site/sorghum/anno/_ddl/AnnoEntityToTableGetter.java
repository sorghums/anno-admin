package site.sorghum.anno._ddl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Named;
import lombok.Setter;
import org.noear.wood.wrap.ColumnWrap;
import org.noear.wood.wrap.TableWrap;
import site.sorghum.anno._ddl.entity2db.EntityToTableGetter;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * anno 的实体类，转成 table
 *
 * @author songyinyin
 * @since 2023/7/4 22:44
 */
@Named
public class AnnoEntityToTableGetter implements EntityToTableGetter<AnEntity> {

    @Setter
    private String defaultPkName = "id";

    @Override
    public TableWrap getTable(AnEntity anEntity) {

        TableWrap tableWrap = new TableWrap(anEntity.getTableName(), anEntity.getTitle());

        List<AnField> fields = new ArrayList<>(anEntity.getDbAnFields());

        // 将 id 字段放到第一位
        AnField idField = CollUtil.findOne(fields, e -> e.getFieldName().equals(defaultPkName));
        fields.removeIf(e -> e.getFieldName().equals(defaultPkName));
        fields.add(0, idField);

        for (AnField field : fields) {
            // 虚拟列，跳过
            if (field.isVirtualColumn()) {
                continue;
            }
            // 不是基本类型，跳过
            if (!columnIsSupport(field.getFieldType())) {
                continue;
            }
            String columnName = field.getTableFieldName();
            if (StrUtil.isBlank(columnName)) {
                // 默认使用小写，下划线命名
                columnName = StrUtil.toUnderlineCase(field.getFieldName());
            }

            int sqlType;
            Integer size = null;
            Integer digit = null;
            String defaultValue = "";

            final ColumnWrap simpleColumn = getColumn(anEntity, field);
            switch (field.getDataType()) {
                // TREE 和 OPTIONS，通过 anno 注解获取不到字段类型，需要通过字段类型获取
                // 默认类型是STRING，也需要通过字段类型获取
                case STRING, TREE, OPTIONS, PICKER -> {
                    sqlType = simpleColumn.getSqlType();
                    size = simpleColumn.getSize();
                    digit = simpleColumn.getDigit();
                }
                case DATE -> sqlType = Types.DATE;
                case DATETIME -> sqlType = Types.TIMESTAMP;
                case NUMBER -> {
                    sqlType = simpleColumn.getSqlType();
                    size = simpleColumn.getSize();
                    digit = simpleColumn.getDigit();
                    defaultValue = simpleColumn.getIsNullable();
                }
                case RICH_TEXT, CODE_EDITOR, TEXT_AREA, MARK_DOWN -> sqlType = Types.CLOB;
                case FILE, IMAGE, LINK -> {
                    sqlType = Types.VARCHAR;
                    size = 1024;
                }
                default -> {
                    sqlType = simpleColumn.getSqlType();
                    size = simpleColumn.getSize();
                    defaultValue = simpleColumn.getIsNullable();
                }
            }

            // 主键
            if (anEntity.getPkField().getFieldName().equals(field.getFieldName())) {
                defaultValue = "NOT NULL";
                tableWrap.addPk(columnName);
                size = 32;
            }

            if (field.getFieldSize() != 0) {
                size = field.getFieldSize();
            }

            if (StrUtil.isNotBlank(field.getDefaultValue())) {
                if (sqlType == Types.VARCHAR || sqlType == Types.CHAR || sqlType == Types.CLOB) {
                    defaultValue = "DEFAULT '" + field.getDefaultValue() + "'";
                } else {
                    defaultValue = "DEFAULT " + field.getDefaultValue();
                }
            }

            ColumnWrap columnWrap = new ColumnWrap(anEntity.getTableName(), columnName, sqlType, size, digit, defaultValue, field.getTitle());
            tableWrap.addColumn(columnWrap);
        }

        return tableWrap;
    }

    public ColumnWrap getColumn(AnEntity anEntity, AnField field) {
        String fieldName = field.getTableFieldName();
        Class<?> fieldType = field.getFieldType();
        int sqlType;
        Integer size = null;
        Integer digit = null;
        String defaultValue = "";
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
            defaultValue = "DEFAULT 0";
        } else if (fieldType == Boolean.class) {
            sqlType = Types.BIT;
            size = 1;
            defaultValue = "DEFAULT 0";
        } else {
            sqlType = Types.VARCHAR;
            size = 512;
        }

        return new ColumnWrap(anEntity.getTableName(), fieldName, sqlType, size, digit, defaultValue, null);
    }

    private boolean columnIsSupport(Class<?> fieldType) {
        return ClassUtil.isBasicType(fieldType)
            || CharSequence.class.isAssignableFrom(fieldType)
            || Number.class.isAssignableFrom(fieldType)
            || Date.class.isAssignableFrom(fieldType)
            || java.sql.Date.class.isAssignableFrom(fieldType)
            || LocalDate.class.isAssignableFrom(fieldType)
            || LocalDateTime.class.isAssignableFrom(fieldType);
    }
}
