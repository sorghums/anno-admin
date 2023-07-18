package site.sorghum.anno.ddl.entity2db;

import org.noear.wood.wrap.TableWrap;

/**
 * 从实体上，获取表结构信息
 *
 * @author songyinyin
 * @since 2023/7/3 22:37
 * @see SampleEntityToTableGetter
 */
public interface EntityToTableGetter<T> {

    TableWrap getTable(T entity);

}
