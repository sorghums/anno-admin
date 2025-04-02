package site.sorghum.anno._metadata;

import cn.hutool.core.bean.BeanUtil;
import jakarta.inject.Named;
import site.sorghum.anno._common.util.MetaClassUtil;

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
        return entityName(entity);
    }

    /**
     * 获取实体类的简单名称。</br>
     * 如果传入的实体类是匿名类，则会获取其实际类的简单名称。
     *
     * @param entity 实体类对象
     * @return 实体类的简单名称
     */
    public static String entityName(Class<?> entity) {
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

}
