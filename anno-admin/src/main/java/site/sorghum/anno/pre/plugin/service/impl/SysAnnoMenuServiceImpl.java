package site.sorghum.anno.pre.plugin.service.impl;

import jakarta.inject.Named;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.pre.plugin.ao.SysAnnoMenu;
import site.sorghum.anno.pre.plugin.dao.SysAnnoMenuDao;
import site.sorghum.anno.pre.plugin.service.SysAnnoMenuService;

import java.util.List;

/**
 * 菜单服务实现类
 *
 * @author Sorghum
 * @since 2023/06/30
 */
@Named
public class SysAnnoMenuServiceImpl implements SysAnnoMenuService {
    @Db
    SysAnnoMenuDao sysAnnoMenuDao;

    @Override
    public List<SysAnnoMenu> list() {
        return sysAnnoMenuDao.list();
    }
}
