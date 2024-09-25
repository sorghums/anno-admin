package site.sorghum.anno.plugin.service.impl;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno.plugin.ao.AnAnnoMenu;
import site.sorghum.anno.plugin.dao.AnAnnoMenuDao;
import site.sorghum.anno.plugin.service.AnnoMenuService;

import java.util.List;

/**
 * 菜单服务实现类
 *
 * @author Sorghum
 * @since 2023/06/30
 */
@Named
public class AnnoMenuServiceImpl implements AnnoMenuService {
    @Inject
    AnAnnoMenuDao anAnnoMenuDao;

    @Override
    public List<AnAnnoMenu> list() {
        return anAnnoMenuDao.bizList();
    }

    @Override
    public AnAnnoMenu getById(String id) {
        return anAnnoMenuDao.findById(id);
    }
}
