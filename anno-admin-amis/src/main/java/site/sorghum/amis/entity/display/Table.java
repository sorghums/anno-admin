package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;

import java.util.List;
import java.util.Map;

/**
 * 表格
 *
 * @author sorghum
 * @since 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Table extends AmisBase {
    {
        setType("table");
    }
    /**
     * 标题
     */
    String title;

    /**
     * 数据源, 绑定当前环境变量
     */
    Object source;

    /**
     * 是否固定表头
     */
    Boolean affixHeader;

    /**
     * 展示列显示开关, 自动即：列数量大于或等于 5 个时自动开启
     */
    String columnsTogglable;

    /**
     * 当没数据的时候的文字提示
     */
    String placeholder;

    /**
     * 外层 CSS 类名
     */
    String className;

    /**
     * 表格 CSS 类名
     */
    String tableClassName;

    /**
     * 顶部外层 CSS 类名
     */
    String headerClassName;

    /**
     * 底部外层 CSS 类名
     */
    String footerClassName;

    /**
     * 工具栏 CSS 类名
     */
    String toolbarClassName;

    /**
     * 用来设置列信息
     */
    List<Map> columns;

    /**
     * 自动合并单元格
     */
    Integer combineNum;

    /**
     * 悬浮行操作按钮组
     */
    List<Action> itemActions;

    /**
     * 配置当前行是否可勾选的条件，要用 表达式
     */
    String itemCheckableOn;

    /**
     * 配置当前行是否可拖拽的条件，要用 表达式
     */
    String itemDraggableOn;

    /**
     * 点击数据行是否可以勾选当前行
     */
    Boolean checkOnItemClick;

    /**
     * 给行添加 CSS 类名
     */
    String rowClassName;

    /**
     * 通过模板给行添加 CSS 类名
     */
    String rowClassNameExpr;

    /**
     * 顶部总结行
     */
    List<Map<String , Object>> prefixRow;

    /**
     * 底部总结行
     */
    List<Map<String , Object>> affixRow;

    /**
     * 行角标配置
     */
    Map<String , Object> itemBadge;

    /**
     * 内容区域自适应高度，可选择自适应、固定高度和最大高度	maxHeight 需要 2.8.0 以上版本
     */
    Object autoFillHeight;

    /**
     * 列宽度是否支持调整
     */
    Boolean resizable;

    /**
     * 支持勾选
     */
    Boolean selectable;

    /**
     * 勾选 icon 是否为多选样式checkbox， 默认为radio
     */
    Boolean multiple;

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Column extends AmisBase{
        /**
         * 表头文本内容
         */
        String label;

        /**
         * 通过名称关联数据
         */
        String name;

        /**
         * 列宽
         */
        String width;

        /**
         * 提示信息
         */
        String remark;

        /**
         * 是否固定当前列
         */
        String fixed;

        /**
         * 弹出框
         */
        String popOver;

        /**
         * 是否可复制
         * boolean | {icon: string, content:string}
         */
        Object copyable;

        /**
         * 单元格自定义样式
         */
        Map<String , Object> style;


        /**
         * 单元格内部组件自定义样式
         */
        Map<String , Object> innerStyle;

        /**
         * 列可排序
         */
        Boolean sortable;
    }
}
