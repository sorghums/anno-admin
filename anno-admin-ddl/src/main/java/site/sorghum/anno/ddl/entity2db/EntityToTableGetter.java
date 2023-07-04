package site.sorghum.anno.ddl.entity2db;

import org.noear.wood.wrap.ColumnWrap;
import org.noear.wood.wrap.TableWrap;

import java.lang.reflect.Field;

/**
 * @author songyinyin
 * @since 2023/7/3 22:37
 */
public interface EntityToTableGetter {

  TableWrap getTable(Class<?> clazz);

  ColumnWrap getColumn(Class<?> clazz, Field field);
}
