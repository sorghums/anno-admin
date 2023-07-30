package site.sorghum.anno.modular.menu.entity.proxy;

import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.metadata.AnEntity;
import site.sorghum.anno.metadata.MetadataManager;
import site.sorghum.anno.modular.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.modular.anno.util.AnnoClazzCache;
import site.sorghum.anno.modular.menu.entity.anno.SysAnnoMenu;

import java.util.Collection;
import java.util.List;

/**
 * 菜单代理
 *
 * @author Sorghum
 * @since 2023/07/03
 */
@Named
public class SysAnnoMenuProxy implements AnnoBaseProxy<SysAnnoMenu> {

    @Inject
    MetadataManager metadataManager;

    @Override
    public void beforeAdd(TableParam<SysAnnoMenu> tableParam, SysAnnoMenu data) {
        String parseData = null;
        // ------ 解析菜单解析 ------
        if (StrUtil.isNotBlank(data.getParseData())) {
            parseData = data.getParseData().trim();
        }
        data.setParseData(parseData);
        //1. AnnoMain --> 转成菜单
        if ("annoMain".equals(data.getParseType())) {
            Class<?> targetClazz = AnnoClazzCache.get(data.getParseData());
            AnEntity anEntity = metadataManager.getEntity(targetClazz);
            data.setParseType("anno");
            data.setHref("/system/config/amis/" + data.getParseData());
            data.setTitle(StrUtil.isBlank(data.getTitle()) ? anEntity.getTitle() : data.getTitle());
            data.setOpenType("_iframe");
            data.setType(1);
        }
    }

    // ------ 以下方法暂时不用 ------

    @Override
    public void afterAdd(SysAnnoMenu data) {

    }

    @Override
    public void beforeUpdate(TableParam<SysAnnoMenu> tableParam, List<DbCondition> dbConditions, SysAnnoMenu data) {

    }

    @Override
    public void afterUpdate(SysAnnoMenu data) {

    }

    @Override
    public void beforeDelete(TableParam<SysAnnoMenu> tableParam, List<DbCondition> dbConditions) {

    }

    @Override
    public void afterDelete(List<DbCondition> dbConditions) {

    }

    @Override
    public void beforeFetch(TableParam<SysAnnoMenu> tableParam, List<DbCondition> dbConditions, PageParam pageParam) {

    }

    @Override
    public void afterFetch(Collection<SysAnnoMenu> dataList) {

    }
}
