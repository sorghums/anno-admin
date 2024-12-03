package site.sorghum.anno.anno.annocache;

import jakarta.inject.Named;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataContext;
import site.sorghum.anno.anno.util.AnnoClazzCache;
import site.sorghum.anno.anno.util.AnnoFieldCache;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@Named
public class AnnoCacheContext implements MetadataContext {


    @Override
    public void refresh(List<AnEntity> allEntities) {
        for (AnEntity anEntity : allEntities) {
            Class<?> clazz = anEntity.getThisClass();
            // 缓存处理类
            AnnoClazzCache.put(clazz.getSimpleName(), clazz);
            // 缓存字段信息
            for (AnField field : anEntity.getFields()) {
                String columnName = field.getTableFieldName();
                AnnoFieldCache.putFieldName2FieldAndSql(clazz, columnName, field.getJavaName());
                // 同时保存其实际节点的类的字段信息
                Field javaField = field.getJavaField();
                if (Objects.isNull(javaField)) {
                    continue;
                }
                if (clazz != javaField.getDeclaringClass()) {
                    AnnoFieldCache.putFieldName2FieldAndSql(javaField.getDeclaringClass(), columnName, field.getJavaName());
                }
                if (field.isPkField()) {
                    AnnoFieldCache.putClass2PkName(clazz, field.getJavaName());
                    // 同时保存其实际节点的类的字段信息
                    if (clazz != javaField.getDeclaringClass()) {
                        AnnoFieldCache.putClass2PkName(javaField.getDeclaringClass(), field.getJavaName());
                    }
                }
            }
        }
    }
}
