package site.sorghum.anno.suppose.proxy;

import cn.hutool.core.util.IdUtil;
import jakarta.inject.Named;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.suppose.model.PrimaryKeyModel;

import java.util.List;

/**
 * @author songyinyin
 * @since 2023/8/18 19:08
 */
@Named
public class PrimaryKeyPreProxy implements AnnoBaseProxy<PrimaryKeyModel> {

    @Override
    public String[] supportEntities() {
        return new String[]{
            AnnoBaseProxy.clazzToDamiEntityName(PrimaryKeyModel.class)
        };
    }

    @Override
    public void beforeAdd(PrimaryKeyModel data) {
        data.setId(IdUtil.getSnowflakeNextIdStr());
    }
}
