package site.sorghum.anno.plugin;


import cn.hutool.core.io.resource.MultiResource;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.ExtensionPoint;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.method.resource.ResourceFinder;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Anno模块
 *
 * @author sorghum
 * @since 2023/07/15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public abstract class AnnoPlugin implements ExtensionPoint {

    /**
     * 模块名称（菜单名称）
     */
    public String pluginName;

    /**
     * 模块描述
     */
    public String pluginDesc;

    /**
     * 默认图标
     */
    private static final String DEFAULT_ICON = "ant-design:bars-outlined";

    /**
     * 执行顺序，越大越先执行
     */
    public int runOrder() {
        return 10;
    }

    public AnnoPlugin(String pluginName, String pluginDesc) {
        this.pluginName = pluginName;
        this.pluginDesc = pluginDesc;
    }

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
        if (StrUtil.isBlank(icon)) {
            icon = DEFAULT_ICON;
        }
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
    public AnPluginMenu createEntityMenu(Class<?> entityClass, String parentId, String icon, Integer sort) {
        return createEntityMenu(entityClass, parentId, null, icon, sort);
    }

    /**
     * 创建实体菜单（二级）
     *
     * @param entityClass 实体类
     * @param parentId    父级菜单id
     * @param icon        菜单图标
     * @param sort        菜单排序
     */
    public AnPluginMenu createEntityMenu(String entityClass, String parentId, String icon, Integer sort) {
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
    public AnPluginMenu createEntityMenu(Class<?> entityClass, String parentId, String title, String icon, Integer sort) {
        AnPluginMenu anPluginMenu = new AnPluginMenu();
        anPluginMenu.setTitle(title);
        if (StrUtil.isBlank(icon)) {
            icon = DEFAULT_ICON;
        }
        anPluginMenu.setIcon(icon);
        anPluginMenu.setSort(sort);
        anPluginMenu.setType(1);
        anPluginMenu.setParentId(parentId);
        anPluginMenu.setEntity(AnnoBeanUtils.getBean(MetadataManager.class).getEntity(entityClass));
        return anPluginMenu;
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
    public AnPluginMenu createEntityMenu(String entityClass, String parentId, String title, String icon, Integer sort) {
        AnPluginMenu anPluginMenu = new AnPluginMenu();
        anPluginMenu.setTitle(title);
        if (StrUtil.isBlank(icon)) {
            icon = DEFAULT_ICON;
        }
        anPluginMenu.setIcon(icon);
        anPluginMenu.setSort(sort);
        anPluginMenu.setType(1);
        anPluginMenu.setParentId(parentId);
        anPluginMenu.setEntity(AnnoBeanUtils.getBean(MetadataManager.class).getEntity(entityClass));
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

    public List<String> xmlPath() {
        return Collections.emptyList();
    }

    public void loadXml() {
        List<String> xmlList = xmlPath();
        if (xmlList.isEmpty()) {
            return;
        }
        for (String xmlPath : xmlList) {
            MultiResource multiResource = ResourceFinder.of().find(xmlPath);
            multiResource.iterator().forEachRemaining(resource -> {
                try {
                    log.info("从xml加载配置：{}", resource.getUrl());
                    String content = resource.getReader(Charset.defaultCharset()).lines().collect(Collectors.joining());
                    AnnoBeanUtils.getBean(MetadataManager.class).loadEntityByXml(content, true);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
        AnnoBeanUtils.getBean(MetadataManager.class).refresh();
    }
}
