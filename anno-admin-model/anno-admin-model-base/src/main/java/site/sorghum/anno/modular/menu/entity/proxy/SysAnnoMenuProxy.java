package site.sorghum.anno.modular.menu.entity.proxy;

import cn.hutool.core.util.StrUtil;
import org.noear.solon.annotation.ProxyComponent;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.modular.anno.util.AnnoClazzCache;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import site.sorghum.anno.modular.menu.entity.anno.SysAnnoMenu;

/**
 * 菜单代理
 *
 * @author Sorghum
 * @since 2023/07/03
 */
@ProxyComponent
public class SysAnnoMenuProxy extends AnnoBaseProxy<SysAnnoMenu> {

    @Override
    public void beforeAdd(SysAnnoMenu data) {
        // ------ 解析菜单解析 ------
        String parseData = data.getParseData().trim();
        data.setParseData(parseData);
        //1. AnnoMain --> 转成菜单
        if ("annoMain".equals(data.getParseType())) {
            Class<?> targetClazz = AnnoClazzCache.get(data.getParseData());
            AnnoMain annoMain = AnnoUtil.getAnnoMain(targetClazz);
            data.setParseType("anno");
            data.setHref("/system/config/amis/"+data.getParseData());
            data.setTitle(StrUtil.isBlank(data.getTitle()) ? annoMain.name() : data.getTitle());
            data.setOpenType("_iframe");
            data.setType(1);
        }
    }
}