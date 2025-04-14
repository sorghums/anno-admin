package site.sorghum.anno._ddl.entity2db;

import site.sorghum.ddl.entity.DdlTableWrap;

/**
 * 从实体上，获取表结构信息
 *
 * @author songyinyin
 * @since 2023/7/3 22:37
 * @see SampleEntityToTableGetter
 */
public interface EntityToTableGetter<T> {

    DdlTableWrap getTable(T entity);

}
