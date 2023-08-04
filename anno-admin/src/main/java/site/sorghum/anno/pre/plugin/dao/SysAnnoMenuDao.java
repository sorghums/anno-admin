package site.sorghum.anno.pre.plugin.dao;

import org.noear.wood.annotation.Sql;
import org.noear.wood.xml.Namespace;
import site.sorghum.anno.pre.plugin.ao.SysAnnoMenu;
import site.sorghum.anno.pre.suppose.AnnoBaseMapper;

import java.util.List;

@Namespace("site.sorghum.anno.modular.system.dao")
public interface SysAnnoMenuDao extends AnnoBaseMapper<SysAnnoMenu> {

    /**
     * 列表
     *
     * @return {@link List}<{@link SysAnnoMenu}>
     */
    @Sql("select * from sys_anno_menu where del_flag = 0 order by sort")
    List<SysAnnoMenu> list();
}
