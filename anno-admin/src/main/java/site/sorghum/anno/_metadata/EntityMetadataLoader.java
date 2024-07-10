package site.sorghum.anno._metadata;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.bean.BeanUtil;
import jakarta.inject.Named;
import site.sorghum.anno._common.util.MetaClassUtil;
import site.sorghum.anno.anno.annotation.field.AnnoMany2ManyField;
import site.sorghum.anno.anno.util.AnnoUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * anno 实体 元数据加载
 *
 * @author songyinyin
 * @since 2023/7/15 19:21
 */
@Named
public class EntityMetadataLoader implements MetadataLoader<Class<?>> {

    @Override
    public String getEntityName(Class<?> entity) {
        // 如果是匿名类,取实际类
        while (entity.isAnonymousClass()) {
            entity = entity.getSuperclass();
        }
        return entity.getSimpleName();
    }

    /**
     * 加载AnEntity对象
     *
     * @param clazz 类对象
     * @return AnEntity对象
     */
    @Override
    public AnEntity load(Class<?> clazz) {
        AnEntity anEntity = new AnEntity();
        AnMeta anMeta = MetaClassUtil.class2AnMeta(clazz);
        BeanUtil.copyProperties(anMeta, anEntity);
        return anEntity;
    }

    /**
     * 加载AnEntity对象
     *
     * @param clazz 类对象
     * @return AnEntity对象
     */
    @Override
    public AnEntity loadForm(Class<?> clazz) {
        AnEntity anEntity = new AnEntity();
        AnMeta anMeta = MetaClassUtil.class2AnMeta(clazz);
        BeanUtil.copyProperties(anMeta, anEntity);
        return anEntity;
    }

    /**
     * 设置AnEntity对象的多对多字段
     *
     * @param entity AnEntity对象
     * @param clazz  需要设置多对多字段的类
     */
    private void setAnMany2ManyFields(AnEntity entity, Class<?> clazz) {
        List<Field> fields = AnnoUtil.getAnnoMany2ManyFields(clazz);
        List<AnMany2ManyField> annoMany2ManyFields = new ArrayList<>();
        for (Field field : fields) {
            AnnoMany2ManyField anno = AnnotationUtil.getAnnotation(field, AnnoMany2ManyField.class);
            AnMany2ManyField anMany2ManyField = parseAnMany2ManyField(field, anno);

            annoMany2ManyFields.add(anMany2ManyField);
        }
//        entity.setMany2ManyFields(annoMany2ManyFields);
    }

    /**
     * 根据给定的Field和AnnoMany2ManyField注解获取AnMany2ManyField对象
     *
     * @param field 给定的Field对象
     * @param anno 给定的AnnoMany2ManyField注解对象
     * @return 返回一个AnMany2ManyField对象
     */
    private static AnMany2ManyField parseAnMany2ManyField(Field field, AnnoMany2ManyField anno) {
        AnMany2ManyField anMany2ManyField = new AnMany2ManyField();
        anMany2ManyField.setField(field);
        anMany2ManyField.setMediumTable(anno.mediumTable());

        anMany2ManyField.setOtherColumnMediumName(anno.otherColumn().mediumName());
        anMany2ManyField.setOtherColumnReferencedName(anno.otherColumn().referencedName());

        anMany2ManyField.setThisColumnMediumName(anno.thisColumn().mediumName());
        anMany2ManyField.setThisColumnReferencedName(anno.thisColumn().referencedName());
        return anMany2ManyField;
    }

}
