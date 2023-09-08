package site.sorghum.anno.plugin.service;

import site.sorghum.anno.plugin.ao.AnAnnoMenu;

import java.util.List;

/**
 *菜单服务接口类
 *
 * @author Sorghum
 * @since 2023/06/30
 */
public interface SysAnnoMenuService {
    /**
     * 列表
     *
     * @return {@link List}<{@link AnAnnoMenu}>
     */
    List<AnAnnoMenu> list();
}
