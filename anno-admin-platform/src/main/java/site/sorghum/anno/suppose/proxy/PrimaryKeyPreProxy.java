package site.sorghum.anno.suppose.proxy;

import cn.hutool.core.util.IdUtil;
import org.noear.solon.annotation.Component;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.suppose.model.PrimaryKeyModel;

/**
 * @author songyinyin
 * @since 2023/8/18 19:08
 */
@Component
@org.springframework.stereotype.Component
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
