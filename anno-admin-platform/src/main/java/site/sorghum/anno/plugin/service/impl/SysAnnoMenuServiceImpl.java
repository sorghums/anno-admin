package site.sorghum.anno.plugin.service.impl;

import jakarta.inject.Named;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.plugin.ao.AnAnnoMenu;
import site.sorghum.anno.plugin.dao.AnAnnoMenuDao;
import site.sorghum.anno.plugin.service.SysAnnoMenuService;

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
    AnAnnoMenuDao anAnnoMenuDao;

    @Override
    public List<AnAnnoMenu> list() {
        return anAnnoMenuDao.list();
    }
}
