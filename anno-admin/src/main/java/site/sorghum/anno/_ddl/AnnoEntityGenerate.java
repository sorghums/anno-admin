package site.sorghum.anno._ddl;

import cn.hutool.core.util.ClassUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.noear.wood.annotation.Column;
import org.noear.wood.annotation.Exclude;
import org.noear.wood.annotation.PrimaryKey;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.ddl.entity.DdlColumnWrap;
import site.sorghum.ddl.entity.DdlTableWrap;
import site.sorghum.ddl.generate.JavaCoreEntityGenerate;

import java.lang.reflect.Field;
import java.sql.JDBCType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Named
public class AnnoEntityGenerate extends JavaCoreEntityGenerate {

    @Inject
    MetadataManager metadataManager;

    /**
     * Java的类 -> 数据库表元数据
     *
     * @param tClass 类名
     * @return 数据库表元数据
     */
    @Override
    public DdlTableWrap get(Class<?> tClass) {
        DdlTableWrap coreDdlTableWrap = getCoreDdlTableWrap(tClass);
        AnEntity managerEntity = metadataManager.getEntity(tClass);
        String tableName = managerEntity.tableName();
        if (tableName != null && !tableName.isEmpty()) {
            coreDdlTableWrap.setName(tableName);
        }
        List<AnField> fields = managerEntity.getFields();
        for (AnField field : fields) {
            // 虚拟列，跳过
            if (field.isVirtualColumn()) {
                continue;
            }
            // 不是基本类型，跳过
            if (!columnIsSupport(field.getJavaType())) {
                continue;
            }
            // -------- wood 相关设置 --------
            Field javaField = field.getJavaField();
            DdlColumnWrap coreDdlColumnWrap = getCoreDdlColumnWrap(coreDdlTableWrap.getName(), javaField, coreDdlTableWrap.getPks());
            if (coreDdlColumnWrap == null) {
                continue;
            }
            Exclude exclude = javaField.getAnnotation(Exclude.class);
            if (exclude != null) {
                continue;
            }
            Column columnAnnotation = javaField.getAnnotation(Column.class);
            if (columnAnnotation != null && columnAnnotation.value() != null && !columnAnnotation.value().isEmpty()) {
                coreDdlColumnWrap.setName(columnAnnotation.value());
            }
            PrimaryKey primaryKey = javaField.getAnnotation(PrimaryKey.class);
            if (primaryKey != null) {
                boolean contains = coreDdlTableWrap.getPks().contains(coreDdlColumnWrap.getName());
                if (!contains) {
                    coreDdlTableWrap.getPks().add(coreDdlColumnWrap.getName());
                }
            }

            if (field.getTableFieldName() != null && !field.getTableFieldName().isEmpty()){
                coreDdlColumnWrap.setName(field.getTableFieldName());
            }

            switch (field.getDataType()) {
                case RICH_TEXT, CODE_EDITOR, TEXT_AREA, MARK_DOWN -> {
                    coreDdlColumnWrap.setDdlType(JDBCType.BLOB);
                }
                case FILE, IMAGE, LINK -> {
                    coreDdlColumnWrap.setSize(1024);
                }
            }
            if (field.pkField()) {
                boolean contains = coreDdlTableWrap.getPks().contains(coreDdlColumnWrap.getName());
                if (!contains) {
                    coreDdlTableWrap.getPks().add(coreDdlColumnWrap.getName());
                }
            }
            coreDdlColumnWrap.setRemarks(field.getTitle());
            coreDdlColumnWrap.setDefaultValue(field.title());
            coreDdlTableWrap.getColumns().add(coreDdlColumnWrap);
        }
        return coreDdlTableWrap;
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
