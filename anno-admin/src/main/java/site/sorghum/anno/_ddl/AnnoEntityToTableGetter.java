package site.sorghum.anno._ddl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
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
            // 不是基本类型，跳过
            if (!columnIsSupport(field.getFieldType())) {
                continue;
            }
            String columnName = field.getTableFieldName();
            if (StrUtil.isBlank(columnName)) {
                // 虚拟列，跳过
                continue;
            }

            int sqlType;
            Integer size = null;
            Integer digit = null;
            String defaultValue = "DEFAULT NULL";

            switch (field.getDataType()) {
                // TREE 和 OPTIONS，通过 anno 注解获取不到字段类型，需要通过字段类型获取
                // 默认类型是STRING，也需要通过字段类型获取
                case STRING, TREE, OPTIONS, PICKER -> {
                    ColumnWrap column = getColumn(anEntity, field);
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
                case RICH_TEXT, CODE_EDITOR -> sqlType = Types.CLOB;
                default -> { // FILE, IMAGE and others
                    sqlType = Types.VARCHAR;
                    size = 512;
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
                defaultValue = field.getDefaultValue();
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
        } else if (fieldType == Boolean.class) {
            sqlType = Types.BIT;
            size = 1;
            defaultValue = "NOT NULL DEFAULT 0";
        } else {
            throw new DdlException("%s.%s 不支持的字段类型：%s".formatted(anEntity.getEntityName(), field.getFieldName(), fieldType.getSimpleName()));
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
