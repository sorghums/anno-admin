package site.sorghum.anno.suppose.mapper;

import org.noear.wood.ext.Fun2;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno.db.TableParam;

/**
 * baseMapper 中 entity 转数据库字段时，过滤掉不需要的字段
 *
 * @author songyinyin
 * @since 2023/8/5 11:14
 */
public class EntityFieldFilter implements Fun2<Boolean, String, Object> {

    private final Class<?> entityClz;

    /**
     * 是否排除字段值为 null 的字段
     */
    private final boolean excludeNull;

    public EntityFieldFilter(Class<?> entityClz, boolean excludeNull) {
        this.entityClz = entityClz;
        this.excludeNull = excludeNull;
    }

    @Override
    public Boolean run(String tableFieldName, Object fieldValue) {
        if (excludeNull && fieldValue == null) {
            return false;
        }
        TableParam tableParam = AnnoBeanUtils.metadataManager().getTableParamImmutable(entityClz);
        if (tableParam != null) {
            return tableParam.getColumns().contains(tableFieldName);
        }
        return true;
    }
}
