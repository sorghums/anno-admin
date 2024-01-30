package site.sorghum.anno.plugin.dao;

import jakarta.inject.Named;
import site.sorghum.anno.db.dao.AnnoBaseDao;
import site.sorghum.anno.plugin.ao.AnAnnoMenu;

import java.util.List;

/**
 * anno菜单dao
 *
 * @author Sorghum
 * @since 2024/01/30
 */
@Named
public class AnAnnoMenuDao implements AnnoBaseDao<AnAnnoMenu> {

    /**
     * 列表
     *
     * @return {@link List}<{@link AnAnnoMenu}>
     */
    public List<AnAnnoMenu> bizList(){
        return this.sqlList(
            "select * from an_anno_menu where del_flag = 0 order by sort"
        );
    }
}
