package site.sorghum.anno.plugin.dao;

import org.noear.wood.annotation.Sql;
import org.noear.wood.xml.Namespace;
import site.sorghum.anno.plugin.ao.AnAnnoMenu;
import site.sorghum.anno.suppose.mapper.AnnoBaseMapper;

import java.util.List;

@Namespace("site.sorghum.anno.modular.system.dao")
public interface AnAnnoMenuDao extends AnnoBaseMapper<AnAnnoMenu> {

    /**
     * 列表
     *
     * @return {@link List}<{@link AnAnnoMenu}>
     */
    @Sql("select * from an_anno_menu where del_flag = 0 order by sort")
    List<AnAnnoMenu> list();
}
