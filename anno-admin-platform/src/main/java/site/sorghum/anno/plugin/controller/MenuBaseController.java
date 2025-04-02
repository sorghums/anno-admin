package site.sorghum.anno.plugin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno.anno.proxy.PermissionProxy;
import site.sorghum.anno.auth.AnnoStpUtil;
import site.sorghum.anno.plugin.ao.AnAnnoMenu;
import site.sorghum.anno.plugin.entity.response.AnAnnoMenuResponse;
import site.sorghum.anno.plugin.entity.response.ReactMenu;
import site.sorghum.anno.plugin.entity.response.VbenMenu;
import site.sorghum.anno.plugin.function.AuthFunction;
import site.sorghum.anno.plugin.service.AnnoMenuService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单控制器
 * 处理菜单数据的获取、转换和权限过滤
 */
@Named
public class MenuBaseController {

    private static final String LAYOUT_COMPONENT = "LAYOUT";
    private static final String DASHBOARD_PATH = "/dashboard";
    private static final String DASHBOARD_NAME = "Analysis";
    private static final String DASHBOARD_COMPONENT = "/dashboard/analysis/index";
    private static final String TOKEN_PLACEHOLDER = "[[[token]]]";
    private static final String API_SERVER_PLACEHOLDER = "[[[apiServerUrl]]]";

    @Inject
    private AnnoMenuService annoMenuService;
    @Inject
    private PermissionProxy permissionProxy;
    @Inject
    private AnnoProperty annoProperty;

    /**
     * 将列表转换为树形结构(AnAnnoMenuResponse类型)
     *
     * @param list 菜单列表
     * @return 树形菜单
     */
    private static List<AnAnnoMenuResponse> convertToTree(List<AnAnnoMenuResponse> list) {
        return buildTree(list, AnAnnoMenuResponse::getId, AnAnnoMenuResponse::getParentId, AnAnnoMenuResponse::getChildren);
    }

    /**
     * 将列表转换为树形结构(VbenMenu类型)
     *
     * @param list 菜单列表
     * @return 树形菜单
     */
    private static List<VbenMenu> convertToVbenTree(List<VbenMenu> list) {
        // 确保所有节点都有sort值
        list.forEach(menu -> menu.setSort(Optional.ofNullable(menu.getSort()).orElse(0)));

        // 排序
        list.sort(Comparator.comparing(VbenMenu::getSort).reversed());

        List<VbenMenu> tree = buildTree(list, VbenMenu::getId, VbenMenu::getParentId, VbenMenu::getChildren);

        // 处理特殊节点
        processSpecialNodes(tree);

        // 添加默认工作台节点
        tree.add(0, createDefaultDashboardMenu());

        return tree;
    }

    /**
     * 处理特殊节点逻辑
     */
    private static void processSpecialNodes(List<VbenMenu> tree) {
        tree.forEach(menu -> {
            if (LAYOUT_COMPONENT.equals(menu.getComponent()) && menu.getChildren().isEmpty()) {
                menu.setHideMenu(true);
                menu.setDisabled(true);
            }
        });

        // 移除无效节点
        tree.removeIf(menu -> LAYOUT_COMPONENT.equals(menu.getComponent()) && menu.getChildren().isEmpty());
    }

    /**
     * 创建默认的工作台菜单
     *
     * @return VbenMenu对象
     */
    private static VbenMenu createDefaultDashboardMenu() {
        VbenMenu menu = new VbenMenu();
        menu.setPath(DASHBOARD_PATH);
        menu.setName(DASHBOARD_NAME);
        menu.setComponent(DASHBOARD_COMPONENT);

        VbenMenu.VbenMeta meta = new VbenMenu.VbenMeta();
        meta.setTitle("工作台");
        meta.setIcon("bx:bx-home");
        menu.setMeta(meta);

        return menu;
    }

    /**
     * 通用树形结构构建方法
     *
     * @param list           原始列表
     * @param idGetter       ID获取函数
     * @param parentIdGetter 父ID获取函数
     * @param childrenGetter 子节点获取函数
     * @param <T>            节点类型
     * @return 树形结构
     */
    private static <T> List<T> buildTree(List<T> list,
                                         java.util.function.Function<T, String> idGetter,
                                         java.util.function.Function<T, String> parentIdGetter,
                                         java.util.function.Function<T, List<T>> childrenGetter) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, T> map = new HashMap<>();
        List<T> roots = new ArrayList<>();

        // 构建节点映射
        list.forEach(node -> {
            if (node != null) {
                map.put(idGetter.apply(node), node);
            }
        });

        // 构建树结构
        list.forEach(node -> {
            if (node != null) {
                String parentId = parentIdGetter.apply(node);
                if (isRootNode(parentId)) {
                    roots.add(node);
                } else {
                    T parent = map.get(parentId);
                    if (parent != null && childrenGetter.apply(parent) != null) {
                        childrenGetter.apply(parent).add(node);
                    }
                }
            }
        });

        return roots;
    }

    /**
     * 将AnAnnoMenu列表转换为AnAnnoMenuResponse列表
     *
     * @param menus 原始菜单列表
     * @return 转换后的菜单列表
     */
    private static List<AnAnnoMenuResponse> listToAnnoMenuResponse(List<AnAnnoMenu> menus) {
        return menus.stream()
            .filter(Objects::nonNull)
            .map(menu -> {
                AnAnnoMenuResponse response = new AnAnnoMenuResponse();
                BeanUtil.copyProperties(menu, response);
                response.setChildren(new ArrayList<>());
                return response;
            })
            .collect(Collectors.toList());
    }

    /**
     * 将AnAnnoMenu列表转换为ReactMenu列表
     *
     * @param menus 原始菜单列表
     * @return 转换后的菜单列表
     */
    private static List<ReactMenu> listToReactMenuResponse(List<AnAnnoMenu> menus) {
        return menus.stream()
            .filter(Objects::nonNull)
            .map(menu -> {
                ReactMenu reactMenu = ReactMenu.toVueMenu(menu);
                reactMenu.setChildren(new ArrayList<>());
                return reactMenu;
            })
            .collect(Collectors.toList());
    }

    /**
     * 将AnAnnoMenu列表转换为VbenMenu列表
     *
     * @param menus 原始菜单列表
     * @return 转换后的菜单列表
     */
    private static List<VbenMenu> listToVbenMenuResponse(List<AnAnnoMenu> menus) {
        return menus.stream()
            .filter(Objects::nonNull)
            .map(menu -> {
                VbenMenu vbenMenu = VbenMenu.toVbenMenu(menu);
                vbenMenu.setChildren(new ArrayList<>());
                return vbenMenu;
            })
            .collect(Collectors.toList());
    }

    /**
     * 判断是否为根节点
     *
     * @param value 节点ID值
     * @return 是否为根节点
     */
    private static boolean isRootNode(Object value) {
        return ObjectUtil.isEmpty(value) || "0".equals(value.toString());
    }

    /**
     * 获取树形结构的菜单数据
     *
     * @return 树形菜单列表
     */
    public List<AnAnnoMenuResponse> dataMenu() {
        List<AnAnnoMenu> menuList = getFilteredMenus();
        return convertToTree(listToAnnoMenuResponse(menuList));
    }

    /**
     * 获取Vben风格的菜单树
     *
     * @return Vben菜单树
     */
    public AnnoResult<List<VbenMenu>> vbenMenu() {
        List<AnAnnoMenu> menuList = getFilteredMenus();
        return AnnoResult.succeed(convertToVbenTree(listToVbenMenuResponse(menuList)));
    }

    /**
     * 获取按钮权限配置
     *
     * @return 按钮权限配置
     */
    public AnnoResult<Map> anButton() {
        String buttonConfig = """
            {
                "useHooks": {
                    "add": true,
                    "delete": true
                }
            }
            """;
        return AnnoResult.succeed(JSONUtil.toBean(buttonConfig, Map.class));
    }

    /**
     * 获取经过权限过滤的菜单列表
     *
     * @return 过滤后的菜单列表
     */
    private List<AnAnnoMenu> getFilteredMenus() {
        // 登录校验
        permissionProxy.checkLogin();

        List<AnAnnoMenu> allMenus = annoMenuService.list();
        String userId = AnnoStpUtil.isLogin() ? AnnoStpUtil.getLoginIdAsString() : null;
        String tokenValue = AnnoStpUtil.getTokenValue();
        String apiServerUrl = annoProperty.getApiServerUrl();

        // 权限过滤
        List<AnAnnoMenu> filteredMenus = allMenus.stream()
            .filter(menu -> hasPermission(menu, userId))
            .collect(Collectors.toList());

        // 处理菜单数据
        processMenuData(filteredMenus, tokenValue, apiServerUrl);

        return filteredMenus;
    }

    /**
     * 检查用户是否有菜单权限
     *
     * @param menu   菜单项
     * @param userId 用户ID
     * @return 是否有权限
     */
    private boolean hasPermission(AnAnnoMenu menu, String userId) {
        if (StrUtil.isBlank(menu.getPermissionId())) {
            return true;
        }
        // 未登录用户返回全部菜单
        if (userId == null) {
            return true;
        }
        return AuthFunction.permissionList.apply(userId).contains(menu.getPermissionId());
    }

    /**
     * 处理菜单数据(替换变量、处理TPL模板)
     *
     * @param menus        菜单列表
     * @param tokenValue   token值
     * @param apiServerUrl API服务地址
     */
    private void processMenuData(List<AnAnnoMenu> menus, String tokenValue, String apiServerUrl) {
        menus.forEach(menu -> {
            if (menu == null) {
                return;
            }

            // 替换变量
            if (StrUtil.isNotBlank(menu.getParseData())) {
                menu.setParseData(menu.getParseData()
                    .replace(TOKEN_PLACEHOLDER, Objects.toString(tokenValue, ""))
                    .replace(API_SERVER_PLACEHOLDER, Objects.toString(apiServerUrl, "")));
            }

            // 处理TPL模板
            if (StrUtil.isNotBlank(menu.getParseType()) &&
                AnAnnoMenu.ParseTypeConstant.TPL.equals(menu.getParseType())) {
                String tplPath = jointPath(
                    apiServerUrl,
                    AnnoConstants.BASE_URL,
                    String.format("annoTpl?_tplId=%s&_tokenValue=%s", menu.getParseData(), tokenValue)
                );
                menu.setParseData(tplPath);
            }
        });
    }

    /**
     * 拼接路径(处理斜杠和空格)
     *
     * @param paths 路径片段
     * @return 拼接后的路径
     */
    private String jointPath(Object... paths) {
        if (paths == null || paths.length == 0) {
            return "";
        }

        return Arrays.stream(paths)
            .filter(Objects::nonNull)
            .map(path -> {
                String str = path.toString().trim();
                if (str.startsWith("/")) {
                    str = str.substring(1);
                }
                if (str.endsWith("/")) {
                    str = str.substring(0, str.length() - 1);
                }
                return str;
            })
            .filter(StrUtil::isNotBlank)
            .collect(Collectors.joining("/"));
    }
}