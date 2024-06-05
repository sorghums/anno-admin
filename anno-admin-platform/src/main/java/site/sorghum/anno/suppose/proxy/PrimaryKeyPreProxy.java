package site.sorghum.anno.suppose.proxy;

import cn.hutool.core.util.IdUtil;
import jakarta.inject.Named;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.PrimaryKeyModel;

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
