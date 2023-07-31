package site.sorghum.anno.pre.plugin.service;

import site.sorghum.anno.pre.plugin.ao.SysAnnoMenu;

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
     * @return {@link List}<{@link SysAnnoMenu}>
     */
    List<SysAnnoMenu> list();
}
