package site.sorghum.amis.entity.function;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

/**
 * 行为按钮
 *
 * @author sorghum
 * @date 2023/06/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Action extends AmisBase {
    {
        //初始化部分数据
        setType("action");
    }

    /**
     * 【必填】这是 action 最核心的配置，来指定该 action 的作用类型
     * 支持：ajax、link、url、drawer、dialog、confirm、cancel、prev、next、copy、close。
     */
    String actionType;

    /**
     * 按钮文本。可用 ${xxx} 取值。
     */
    String label;

    /**
     * 按钮样式，支持：link、primary、secondary、info、success、warning、danger、light、dark、default。
     */
    String level = "default";

    /**
     * 按钮大小，支持：xs、sm、md、lg。
     */
    String size;

    /**
     * 设置图标，例如fa fa-plus。
     */
    String icon;

    /**
     * 给图标上添加类名。
     */
    String iconClassName;

    /**
     * 在按钮文本右侧设置图标，例如fa fa-plus。
     */
    String rightIcon;

    /**
     * 给右侧图标上添加类名。
     */
    String rightIconClassName;

    /**
     * 按钮是否高亮。
     */
    Boolean active;

    /**
     * 按钮高亮时的样式，配置支持同level。
     */
    String activeLevel;

    /**
     * 给按钮高亮添加类名。
     */
    String activeClassName = "is-active";

    /**
     * 用display:"block"来显示按钮。
     */
    Boolean block;

    /**
     * 当设置后，操作在开始前会询问用户。可用 ${xxx} 取值。
     */
    String confirmText;

    /**
     * 指定此次操作完后，需要刷新的目标组件名字（组件的name值，自己配置的），多个请用 , 号隔开。
     */
    String reload;

    /**
     * 鼠标停留时弹出该段文字，也可以配置对象类型：字段为title和content。可用 ${xxx} 取值。
     */
    String tooltip;

    /**
     * 被禁用后鼠标停留时弹出该段文字，也可以配置对象类型：字段为title和content。可用 ${xxx} 取值。
     */
    String disabledTip;

    /**
     * 如果配置了tooltip或者disabledTip，指定提示信息位置，可配置top、bottom、left、right。
     */
    String placement = "top";

    /**
     * 当action配置在dialog或drawer的actions中时，
     * 配置为true指定此次操作完后关闭当前dialog或drawer。
     * 当值为字符串，并且是祖先层弹框的名字的时候，会把祖先弹框关闭掉。
     */
    Object close;

    /**
     * 配置字符串数组，指定在form中进行操作之前，需要指定的字段名的表单项通过验证
     */
    String[] required;
}
