package site.sorghum.anno.pre.suppose.mapper;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.noear.wood.BaseMapper;
import org.noear.wood.DataItem;
import org.noear.wood.DbTableQuery;
import org.noear.wood.MapperWhereQ;
import org.noear.wood.ext.Act1;
import org.noear.wood.utils.RunUtils;
import site.sorghum.anno.pre.suppose.model.BaseMetaModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于 wood 的 anno 基类，anno 中必须继承此类
 *
 * @author songyinyin
 * @since 2023/7/25 16:12
 */
public interface AnnoBaseMapper<T extends BaseMetaModel> extends BaseMapper<T> {

    @Override
    default Long insert(T entity, boolean excludeNull) {
        DataItem data = new DataItem();
        setId(entity);
        data.setEntityIf(entity, new EntityFieldFilter(entityClz(), excludeNull));

        return RunUtils.call(() -> getQr().insert(data));
    }

    @Override
    default void insertList(List<T> list) {
        List<DataItem> list2 = new ArrayList<>();
        for (T d : list) {
            setId(d);
            list2.add(new DataItem().setEntityIf(d, new EntityFieldFilter(entityClz(), true)));
        }

        RunUtils.call(() -> getQr().insertList(list2));
    }

    @Override
    default Integer updateById(T entity, boolean excludeNull) {
        DataItem data = new DataItem();

        data.setEntityIf(entity, new EntityFieldFilter(entityClz(), excludeNull));

        Object id = data.get(tablePk());

        return RunUtils.call(() -> getQr().whereEq(tablePk(), id).update(data));
    }

    @Override
    default Integer update(T entity, boolean excludeNull, Act1<MapperWhereQ> c) {
        DataItem data = new DataItem();

        data.setEntityIf(entity, new EntityFieldFilter(entityClz(), excludeNull));

        return RunUtils.call(() -> getQr(c).update(data));
    }

    private void setId(T entity) {
        String id = entity.getId();
        if (StrUtil.isBlank(id)) {
            // 主键自动生成策略
            entity.setId(IdUtil.getSnowflakeNextIdStr());
        }
    }

    /**
     * 获取查询器
     */
    private DbTableQuery getQr() {
        return db().table(tableName());
    }

    /**
     * 获取带条件的查询器
     */
    private DbTableQuery getQr(Act1<MapperWhereQ> c) {
        DbTableQuery qr = db().table(tableName());

        if (c != null) {
            c.run(new MapperWhereQ(qr));
        }

        return qr;
    }
}
