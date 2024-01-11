package site.sorghum.anno.plugin.proxy;

import cn.hutool.core.util.StrUtil;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Component;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.anno.util.AnnoClazzCache;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.plugin.ao.AnAnnoMenu;

import java.util.List;

/**
 * 菜单代理
 *
 * @author Sorghum
 * @since 2023/07/03
 */
@Component
@org.springframework.stereotype.Component
public class AnAnnoMenuProxy implements AnnoBaseProxy<AnAnnoMenu> {

    @Override
    public String[] supportEntities() {
        return new String[]{
            AnnoBaseProxy.clazzToDamiEntityName(AnAnnoMenu.class)
        };
    }

    @Override
    public void beforeAdd(AnAnnoMenu data) {
        String parseData = null;
        // ------ 解析菜单解析 ------
        if (StrUtil.isNotBlank(data.getParseData())) {
            parseData = data.getParseData().trim();
        }
        data.setParseData(parseData);
        // 默认Sort设置
        if (data.getSort() == null){
            data.setSort(0);
        }
    }

    @Override
    public void beforeUpdate(List<DbCondition> dbConditions, AnAnnoMenu data) {
        beforeAdd(data);
    }

}
