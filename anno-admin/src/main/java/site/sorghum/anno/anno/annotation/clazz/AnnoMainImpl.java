package site.sorghum.anno.anno.annotation.clazz;

import lombok.Getter;
import site.sorghum.anno.anno.annotation.field.AnnoChartImpl;

import java.lang.annotation.Annotation;

/**
 * AnnoMain注解 实现类
 * <p>
 * 用于标注主要的模板类
 *
 * @author sorghum
 * @since 2024/07/04
 */
@Getter
public class AnnoMainImpl implements AnnoMain {

    /**
     * 名称
     */
    String name = "";

    /**
     * anno图表
     */
    AnnoChartImpl annoChart = new AnnoChartImpl() {{
        setEnable(false);
    }};

    /**
     * 表名称
     */
    String tableName = "";

    /**
     * 排序
     */
    AnnoOrderImpl[] annoOrder = {};

    /**
     * 权限配置
     */
    AnnoPermissionImpl annoPermission = new AnnoPermissionImpl() {{
        setEnable(false);
    }};

    /**
     * 左树右表配置
     */
    AnnoLeftTreeImpl annoLeftTree = new AnnoLeftTreeImpl() {{
        setEnable(false);
    }};

    /**
     * Anno 树定义
     */
    AnnoTreeImpl annoTree = new AnnoTreeImpl() {{
        setEnable(false);
    }};

    /**
     * Anno 表按钮
     */
    AnnoTableButtonImpl[] annoTableButton = {};

    /**
     * 组织过滤
     * 如果继承自BaseOrgMetaModel
     * 是否[自动过滤] org_id = 当前登录用户的org_id
     */
    boolean orgFilter;

    /**
     * 是否是虚拟表[将不走数据库]
     */
    boolean virtualTable;

    /**
     * 是否可以删除
     */
    boolean canRemove;

    /**
     * 自动维护表结构
     */
    boolean autoMaintainTable;

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public AnnoChart annoChart() {
        return this.annoChart;
    }

    @Override
    public String tableName() {
        return this.tableName;
    }

    @Override
    public AnnoOrder[] annoOrder() {
        return this.annoOrder;
    }

    @Override
    public AnnoPermission annoPermission() {
        return this.annoPermission;
    }

    @Override
    public AnnoLeftTree annoLeftTree() {
        return this.annoLeftTree;
    }

    @Override
    public AnnoTree annoTree() {
        return this.annoTree;
    }

    @Override
    public AnnoTableButton[] annoTableButton() {
        return this.annoTableButton;
    }

    @Override
    public boolean orgFilter() {
        return this.orgFilter;
    }

    @Override
    public boolean virtualTable() {
        return this.virtualTable;
    }

    @Override
    public boolean canRemove() {
        return this.canRemove;
    }

    @Override
    public boolean autoMaintainTable() {
        return this.autoMaintainTable;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnoMain.class;
    }
}
