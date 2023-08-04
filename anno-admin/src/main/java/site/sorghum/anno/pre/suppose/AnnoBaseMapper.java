package site.sorghum.anno.pre.suppose;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.noear.wood.BaseMapper;
import org.noear.wood.DataItem;
import org.noear.wood.DbTableQuery;
import org.noear.wood.utils.RunUtils;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.AnEntity;
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
    default void insertList(List<T> list) {
        List<DataItem> list2 = new ArrayList<>();
        AnEntity entity = AnnoBeanUtils.metadataManager().getEntity(entityClz());
        for (T d : list) {
            String id = d.getId();
            if (StrUtil.isBlank(id)) {
                d.setId(IdUtil.getSnowflakeNextIdStr());
            }
            list2.add(new DataItem().setEntityIf(d, (k, v) -> {
                if (entity == null) {
                    return true;
                }
                return entity.getFieldMap().containsKey(k);
            }));
        }

        RunUtils.call(() -> getQr().insertList(list2));
    }

    private DbTableQuery getQr() {
        return db().table(tableName());
    }
}
