package site.sorghum.anno.anno.annotation.clazz;

import java.lang.annotation.*;

/**
 * AnnoMain注解
 * 用于标注主要的模板类
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface AnnoMain {

    /**
     * 服务名称
     *
     * @return {@link String}
     */
    String name() default "" ;

    /**
     * 排序
     *
     * @return {@link AnnoOrder}
     */
    AnnoOrder annoOrder() default @AnnoOrder();

    /**
     * 代理类
     *
     * @return {@link AnnoProxy}
     */
    AnnoProxy annoProxy() default @AnnoProxy();

    /**
     * 权限配置
     *
     * @return {@link AnnoPermission}
     */
    AnnoPermission annoPermission() default @AnnoPermission(enable = false, baseCode = "", baseCodeTranslate = "");

    /**
     * 左树右表配置
     *
     * @return {@link AnnoLeftTree}
     */
    AnnoLeftTree annoLeftTree() default @AnnoLeftTree(enable = false, catKey = "", treeClass = Object.class);

    /**
     * Anno 树定义
     *
     * @return {@link AnnoTree}
     */
    AnnoTree annoTree() default @AnnoTree(enable = false, parentKey = "", key = "", label = "", displayAsTree = false);

    /**
     * Anno 表按钮
     *
     * @return {@link AnnoTableButton[]}
     */
    AnnoTableButton[] annoTableButton() default {};

    /**
     * 组织过滤
     * 如果继承自BaseOrgMetaModel
     * 是否[自动过滤] org_id = 当前登录用户的org_id
     */
    boolean orgFilter() default false;

    /**
     * 是否是虚拟表[将不走数据库]
     */
    boolean virtualTable() default false;
}
