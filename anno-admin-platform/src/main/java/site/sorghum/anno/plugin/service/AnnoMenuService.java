package site.sorghum.anno.plugin.service;

import site.sorghum.anno.plugin.ao.AnAnnoMenu;

import java.util.List;

/**
 *菜单服务接口类
 *
 * @author Sorghum
 * @since 2023/06/30
 */
public interface AnnoMenuService {
    /**
     * 列表
     *
     * @return {@link List}<{@link AnAnnoMenu}>
     */
    List<AnAnnoMenu> list();

    /**
     * 根据菜单ID获取菜单对象
     *
     * @param id 菜单ID
     * @return 菜单对象，如果未找到则返回null
     */
    AnAnnoMenu getById(String id);
}
