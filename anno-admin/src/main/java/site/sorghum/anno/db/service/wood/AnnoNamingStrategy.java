package site.sorghum.anno.db.service.wood;

import cn.hutool.core.util.StrUtil;
import org.noear.wood.wrap.NamingStrategy;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.util.AnnoFieldCache;

import java.lang.reflect.Field;

public class AnnoNamingStrategy extends NamingStrategy {
    @Override
    public String classToTableName(Class<?> clz) {
        try {
            AnEntity entity = AnnoBeanUtils.getBean(MetadataManager.class).getEntity(clz);
            String tableName = entity.getTableName();
            if (!entity.isVirtualTable() && StrUtil.isNotBlank(tableName)) {
                return tableName;
            }
            return super.classToTableName(clz);
        } catch (Exception ignore) {
            return super.classToTableName(clz);
        }
    }

    @Override
    public String fieldToColumnName(Class<?> clz, Field f) {
        try {
            String column = AnnoFieldCache.getSqlColumnByJavaName(clz, f.getName()).trim();
            if (column.contains(" as ")){
                column = column.split("as")[1].trim();
            }
            return column;
        } catch (Exception ignore) {
            return super.fieldToColumnName(clz, f);
        }
    }
}
