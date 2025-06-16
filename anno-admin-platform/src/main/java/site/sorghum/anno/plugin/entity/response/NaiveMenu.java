package site.sorghum.anno.plugin.entity.response;

import lombok.Data;
import site.sorghum.anno.plugin.ao.AnAnnoMenu;

import java.util.List;

/**
 * 菜单/资源实体类
 * <p>
 * 用于管理系统中的菜单、按钮、API等资源信息
 *
 * @author YourName
 * @since 2023-10-10
 */
@Data
public class NaiveMenu {

    /**
     * 子菜单列表
     */
    List<NaiveMenu> children;
    /**
     * 主键ID
     */
    private String id;
    /**
     * 资源名称
     */
    private String name;
    /**
     * 资源编码（唯一标识）
     */
    private String code;
    /**
     * 资源类型（菜单、按钮、API等）
     */
    private String type;
    /**
     * 父级资源ID
     */
    private String parentId;
    /**
     * 前端路由路径
     */
    private String path;
    /**
     * 重定向路径
     */
    private String redirect;
    /**
     * 图标（前端组件使用的图标标识）
     */
    private String icon;
    /**
     * 前端组件路径
     */
    private String component;
    /**
     * 布局组件
     */
    private String layout;
    /**
     * 是否缓存（KeepAlive）
     */
    private boolean keepAlive;
    /**
     * 资源描述
     */
    private String description;
    /**
     * 是否显示（控制菜单是否在前端显示）
     */
    private boolean show;
    /**
     * 是否启用（控制资源是否可用）
     */
    private boolean enable;
    /**
     * 排序字段
     */
    private Integer order;

    public static NaiveMenu toNaiveMenu(AnAnnoMenu menu) {
        NaiveMenu naiveMenu = new NaiveMenu();
        // 基础字段映射
        naiveMenu.setId(menu.getId());
        naiveMenu.setName(menu.getTitle());
        naiveMenu.setCode(menu.getId());
        naiveMenu.setType("MENU"); // 类型转换
        naiveMenu.setParentId(menu.getParentId());
        naiveMenu.setOrder(menu.getSort());
        naiveMenu.setIcon(menu.getIcon());

        // 路由相关字段
        parseComponent(menu,naiveMenu);

        // 默认值设置
        naiveMenu.setKeepAlive(true);
        naiveMenu.setShow(true);     // 默认显示菜单
        naiveMenu.setEnable(true);   // 默认启用

        // 特殊字段处理
        naiveMenu.setLayout("empty");  // 根据parseType设置布局组件

        return naiveMenu;
    }

    private static void parseComponent(AnAnnoMenu menu, NaiveMenu naiveMenu) {
        switch (menu.getParseType()) {
            case AnAnnoMenu.ParseTypeConstant.ANNO_MAIN -> {
                naiveMenu.setComponent("/src/views/pms/user/index.vue");
                naiveMenu.setPath("/smartCrud/%s".formatted(menu.getParseData()));
            }
            case AnAnnoMenu.ParseTypeConstant.IFRAME -> {
                naiveMenu.setComponent("/src/views/pms/user/index.vue");
                naiveMenu.setPath(menu.getParseData());
            }
            case AnAnnoMenu.ParseTypeConstant.CHART -> {
                naiveMenu.setComponent("/src/views/pms/user/index.vue");
                naiveMenu.setPath("/smartCrud/%s?anEntity=%s".formatted(menu.getParseData(), menu.getParseData()));
            }
        }
        ;
    }

    private static String parseLayout(AnAnnoMenu menu) {
        return "basic".equals(menu.getParseType())
            ? "basic"
            : "self";
    }
}