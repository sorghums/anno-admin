package site.sorghum.anno._metadata;

/**
 * 元数据加载器
 *
 * @author songyinyin
 * @since 2023/7/15 19:16
 */
public interface MetadataLoader<T> {

    /**
     * 获取实体名称，需要全局唯一
     *
     * @param entity 被加载的对象
     * @return 实体名称
     */
    String getEntityName(T entity);

    /**
     * 加载元数据
     *
     * @param entity 被加载的对象
     */
    AnEntity load(T entity);

    AnEntity loadXml(String xmlContent);

    /**
     * 加载元数据
     *
     * @param entity 被加载的对象
     */
    AnEntity loadForm(T entity);
}
