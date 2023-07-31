package site.sorghum.anno.plugin;


import jakarta.inject.Inject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._metadata.MetadataManager;

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
public class AnnoPlugin implements Runnable {

    /**
     * 模块名称（菜单名称）
     */
    public String pluginName;

    /**
     * 模块描述
     */
    public String pluginDesc;

    @Inject
    MetadataManager metadataManager;

    public AnnoPlugin(String pluginName, String pluginDesc) {
        this.pluginName = pluginName;
        this.pluginDesc = pluginDesc;
    }

    @Override
    public void run() {
    }

    /**
     * 打印模块信息
     */
    public void printPluginInfo() {
        log.info("【🚀🚀🚀 ===> AnnoPlugin: {}, 描述: {} 】", pluginName, pluginDesc);
    }


    /**
     * 初始化实体对应的菜单
     */
    public List<AnPluginMenu> initEntityMenus() {
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
    protected AnPluginMenu createRootMenu(String id, String title, String icon, Integer sort) {
        AnPluginMenu anPluginMenu = new AnPluginMenu();
        anPluginMenu.setId(id);
        anPluginMenu.setTitle(title);
        anPluginMenu.setIcon(icon);
        anPluginMenu.setSort(sort);
        anPluginMenu.setType(0);
        return anPluginMenu;
    }

    /**
     * 创建实体菜单（二级）
     *
     * @param entityClass 实体类
     * @param parentId    父级菜单id
     * @param icon        菜单图标
     * @param sort        菜单排序
     */
    protected AnPluginMenu createEntityMenu(Class<?> entityClass, String parentId, String icon, Integer sort) {
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
    protected AnPluginMenu createEntityMenu(Class<?> entityClass, String parentId, String title, String icon, Integer sort) {
        AnPluginMenu anPluginMenu = new AnPluginMenu();
        anPluginMenu.setTitle(title);
        anPluginMenu.setIcon(icon);
        anPluginMenu.setSort(sort);
        anPluginMenu.setType(1);
        anPluginMenu.setParentId(parentId);

        anPluginMenu.setEntity(metadataManager.getEntity(entityClass));
        return anPluginMenu;
    }


    private String rightPadString(String orginalString, int length, String padString) {
        StringBuilder newString = new StringBuilder(orginalString);
        while (newString.length() < length) {
            newString.append(padString);
        }
        return newString.toString();
    }

    private String leftPadString(String orginalString, int length, String padString) {
        StringBuilder newString = new StringBuilder(orginalString);
        while (newString.length() < length) {
            newString.insert(0, padString);
        }
        return newString.toString();
    }
}
