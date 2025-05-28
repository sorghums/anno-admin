package site.sorghum.anno._ddl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.annotation.Column;
import org.noear.wood.annotation.Exclude;
import org.noear.wood.annotation.PrimaryKey;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._ddl.entity2db.EntityToTableGetter;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.ddl.base.JavaCoreEntityGenerate;
import site.sorghum.ddl.entity.DdlColumnWrap;
import site.sorghum.ddl.entity.DdlTableWrap;

import java.lang.reflect.Field;
import java.sql.JDBCType;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class AnnoEntityToTableGetter extends JavaCoreEntityGenerate implements EntityToTableGetter<AnEntity>{


    /**
     * Java的类 -> 数据库表元数据
     *
     * @param tClass 类名
     * @Param 实体元数据信息
     * @return 数据库表元数据
     */
    public DdlTableWrap get(Class<?> tClass,AnEntity managerEntity) {
        DdlTableWrap coreDdlTableWrap = getCoreDdlTableWrap(tClass);
        String tableName = managerEntity.tableName();
        if (tableName != null && !tableName.isEmpty()) {
            coreDdlTableWrap.setName(tableName);
        }
        String name = managerEntity.getName();
        if (name != null && !name.isEmpty()) {
            coreDdlTableWrap.setRemarks(name);
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
            coreDdlColumnWrap.setDefaultValue(null);
            coreDdlTableWrap.getColumns().add(coreDdlColumnWrap);
        }
        return coreDdlTableWrap;
    }


    @Override
    public DdlTableWrap getTable(AnEntity anEntity) {
        DdlTableWrap ddlTableWrap = get(anEntity.getClass(), anEntity);

        List<String> pks = ddlTableWrap.getPks();
        if (pks == null || pks.isEmpty()){
            throw new BizException("[AnnoAdmin]实体类:%s 无主键配置.".formatted(anEntity.getEntityName()));
        }
        if (pks.size() > 1){
            throw new BizException("[AnnoAdmin]实体类:%s 主键配置错误，只能配置一个主键.".formatted(anEntity.getEntityName()));
        }
        DdlColumnWrap idColumnWrap = CollUtil.findOne(ddlTableWrap.getColumns(), i -> pks.contains(i.getName()));
        ddlTableWrap.getColumns().removeIf(i -> pks.contains(i.getName()));
        ddlTableWrap.getColumns().add(0, idColumnWrap);
        return ddlTableWrap;
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
