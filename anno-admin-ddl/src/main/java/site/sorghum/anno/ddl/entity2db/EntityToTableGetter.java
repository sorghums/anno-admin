package site.sorghum.anno.ddl.entity2db;

import org.noear.wood.wrap.TableWrap;

/**
 * @author songyinyin
 * @since 2023/7/3 22:37
 */
public interface EntityToTableGetter {

  TableWrap getTable(Class<?> clazz);

}
