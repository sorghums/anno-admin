package site.sorghum.anno._ddl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._ddl.entity2db.EntityToTableGetter;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno.anno.annotation.field.AnnoFieldImpl;
import site.sorghum.ddl.defaults.JavaTypeToJdbcTypeConverter;
import site.sorghum.ddl.entity.DdlColumnWrap;
import site.sorghum.ddl.entity.DdlTableWrap;

import java.sql.JDBCType;
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
@Slf4j
public class AnnoEntityToTableGetter implements EntityToTableGetter<AnEntity> {

    @Override
    public DdlTableWrap getTable(AnEntity anEntity) {
        DdlTableWrap tableWrap = new DdlTableWrap(anEntity.getTableName(),
            new ArrayList<>(),anEntity.getName(), new ArrayList<>());
        List<AnField> fields = new ArrayList<>(anEntity.getDbAnFields());

        // 将 id 字段放到第一位
        AnField idField = CollUtil.findOne(fields, AnnoFieldImpl::pkField);
        if(idField == null) {
            throw new BizException("[AnnoAdmin]实体类:%s 无主键配置.".formatted(anEntity.getEntityName()));
        }
        fields.removeIf(e -> e.getJavaName().equals(idField.getJavaName()));
        fields.add(0, idField);
        // 新增主键
        String pkTableName = idField.getTableFieldName();
        if (StrUtil.isBlank(pkTableName)) {
            // 默认使用小写，下划线命名
            pkTableName = StrUtil.toUnderlineCase(idField.getJavaName());
        }
        tableWrap.getPks().add(pkTableName);
        for (AnField field : fields) {
            if (field == null) {
                throw new BizException("AnField is null,AnEntity is " + anEntity);
            }
            // 虚拟列，跳过
            if (field.isVirtualColumn()) {
                continue;
            }
            // 不是基本类型，跳过
            if (!columnIsSupport(field.getJavaType())) {
                continue;
            }
            String defaultValue = "";
            final DdlColumnWrap columnWrap = getColumn(anEntity, field);
            switch (field.getDataType()) {
                case RICH_TEXT, CODE_EDITOR, TEXT_AREA, MARK_DOWN -> {
                    columnWrap.setDdlType(JDBCType.BLOB);
                }
                case FILE, IMAGE, LINK -> {
                    columnWrap.setSize(1024);
                }
            }
            if (field.pkField()){
                columnWrap.setIsNullable(false);
            }
            columnWrap.setRemarks(field.getTitle());
            columnWrap.setDefaultValue(defaultValue);
            tableWrap.getColumns().add(columnWrap);
        }

        return tableWrap;
    }

    public DdlColumnWrap getColumn(AnEntity anEntity, AnField field) {
        boolean pk = field.pkField();
        String columnName = field.getTableFieldName();
        if (StrUtil.isBlank(columnName)) {
            // 默认使用小写，下划线命名
            columnName = StrUtil.toUnderlineCase(field.getJavaName());
        }
        Class<?> fieldType = field.getJavaType();
        Integer size = field.getFieldSize() == 0 ? null: field.getFieldSize();
        Integer digit = null;
        JDBCType jdbcType = JavaTypeToJdbcTypeConverter.getJdbcType(fieldType);
        return new DdlColumnWrap(
            anEntity.getTableName(),
            columnName,
            jdbcType,
            size,
            digit,
            true,
            null,
            null);
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
