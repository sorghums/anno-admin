package site.sorghum.anno.modular.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno.metadata.MetadataManager;

import java.util.List;

/**
 * Anno模块
 *
 * @author sorghum
 * @since 2023/07/15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public class AnnoModule implements Runnable {

    /**
     * 模块名称（菜单名称）
     */
    public String modelName;

    /**
     * 模块描述
     */
    public String modelDesc;

    @Inject
    MetadataManager metadataManager;

    public AnnoModule(String modelName, String modelDesc) {
        this.modelName = modelName;
        this.modelDesc = modelDesc;
    }

    @Override
    public void run() {
        printModelInfo();
    }

    public void printModelInfo() {
        log.info("【🚀🚀🚀 ===> AnnoModule: {}, desc: {} 】", modelName, modelDesc);
    }


    /**
     * 初始化实体对应的菜单
     */
    public List<AnMenu> initEntityMenus() {
        return null;
    }

    /**
     * 创建根菜单（一级）
     *
     * @param id    菜单id
     * @param title 菜单名称
     * @param icon  菜单图标
     * @param sort  菜单排序
     */
    protected AnMenu createRootMenu(String id, String title, String icon, Integer sort) {
        AnMenu anMenu = new AnMenu();
        anMenu.setId(id);
        anMenu.setTitle(title);
        anMenu.setIcon(icon);
        anMenu.setSort(sort);
        anMenu.setType(0);
        return anMenu;
    }

    /**
     * 创建实体菜单（二级）
     *
     * @param entityClass 实体类
     * @param parentId    父级菜单id
     * @param icon        菜单图标
     * @param sort        菜单排序
     */
    protected AnMenu createEntityMenu(Class<?> entityClass, String parentId, String icon, Integer sort) {
        return createEntityMenu(entityClass, parentId, null, icon, sort);
    }

    /**
     * 创建实体菜单（二级）
     *
     * @param entityClass 实体类
     * @param parentId    父级菜单id
     * @param title       菜单名称
     * @param icon        菜单图标
     * @param sort        菜单排序
     */
    protected AnMenu createEntityMenu(Class<?> entityClass, String parentId, String title, String icon, Integer sort) {
        AnMenu anMenu = new AnMenu();
        anMenu.setTitle(title);
        anMenu.setIcon(icon);
        anMenu.setSort(sort);
        anMenu.setType(1);
        anMenu.setParentId(parentId);

        anMenu.setEntity(metadataManager.getEntity(entityClass));
        return anMenu;
    }
}
