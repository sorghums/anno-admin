package site.sorghum.anno.modular.system.service.impl;

import org.noear.solon.annotation.ProxyComponent;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.modular.menu.entity.anno.SysAnnoMenu;
import site.sorghum.anno.modular.system.dao.SysAnnoMenuDao;
import site.sorghum.anno.modular.system.service.SysAnnoMenuService;

import java.util.List;

/**
 * 菜单服务实现类
 *
 * @author Sorghum
 * @since 2023/06/30
 */
@ProxyComponent
public class SysAnnoMenuServiceImpl implements SysAnnoMenuService {
    @Db
    SysAnnoMenuDao sysAnnoMenuDao;

    @Override
    public List<SysAnnoMenu> list() {
        return sysAnnoMenuDao.list();
    }
}
