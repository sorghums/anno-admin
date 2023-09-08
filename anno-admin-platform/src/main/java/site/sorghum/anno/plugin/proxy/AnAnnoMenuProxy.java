package site.sorghum.anno.plugin.proxy;

import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.anno.util.AnnoClazzCache;
import site.sorghum.anno.plugin.ao.AnAnnoMenu;

/**
 * 菜单代理
 *
 * @author Sorghum
 * @since 2023/07/03
 */
@Named
public class AnAnnoMenuProxy implements AnnoBaseProxy<AnAnnoMenu> {

    @Inject
    MetadataManager metadataManager;

    @Override
    public void beforeAdd(AnAnnoMenu data) {
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
}
