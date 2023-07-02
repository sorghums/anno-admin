package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.function.Action;

import java.util.List;
import java.util.Map;

/**
 * 抽屉按钮
 *
 * @author sorghum
 * @date 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DrawerButton extends Action {
    {
        setActionType("drawer");
    }

    /**
     * 抽屉
     */
    Drawer drawer;

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Drawer extends AmisBase {
        {
            setType("drawer");
        }
        //title	SchemaNode		弹出层标题
        //body	SchemaNode		往 Drawer 内容区加内容
        //size	string		指定 Drawer 大小，支持: xs、sm、md、lg、xl
        //position	string		指定 Drawer 方向，支持: left、right、top、bottom
        //className	string	``	Drawer 最外层容器的样式类名
        //headerClassName	string		Drawer 头部 区域的样式类名
        //bodyClassName	string	modal-body	Drawer body 区域的样式类名
        //footerClassName	string		Drawer 页脚 区域的样式类名
        //showCloseButton	boolean	true	是否展示关闭按钮，当值为 false 时，默认开启 closeOnOutside
        //closeOnEsc	boolean	false	是否支持按 Esc 关闭 Drawer
        //closeOnOutside	boolean	false	点击内容区外是否关闭 Drawer
        //overlay	boolean	true	是否显示蒙层
        //resizable	boolean	false	是否可通过拖拽改变 Drawer 大小
        //width	string | number	500px	容器的宽度，在 position 为 left 或 right 时生效
        //height	string | number	500px	容器的高度，在 position 为 top 或 bottom 时生效
        //actions	Array<Action>	【确认】和【取消】	可以不设置，默认只有两个按钮。
        //data	object		支持 数据映射，如果不设定将默认将触发按钮的上下文中继承数据。

        /**
         * 弹出层标题
         */
        String title;

        /**
         * 往 Drawer 内容区加内容
         */
        AmisBase body;

        /**
         * 指定 Drawer 大小，支持: xs、sm、md、lg、xl
         */
        String size;

        /**
         * 指定 Drawer 方向，支持: left、right、top、bottom
         */
        String position = "top";

        /**
         * Drawer 最外层容器的样式类名
         */
        String className;

        /**
         * Drawer 头部 区域的样式类名
         */
        String headerClassName;

        /**
         * Drawer body 区域的样式类名
         */
        String bodyClassName;

        /**
         * Drawer 页脚 区域的样式类名
         */
        String footerClassName;

        /**
         * 是否展示关闭按钮，当值为 false 时，默认开启 closeOnOutside
         */
        boolean showCloseButton = true;

        /**
         * 是否支持按 Esc 关闭 Drawer
         */
        boolean closeOnEsc = false;

        /**
         * 点击内容区外是否关闭 Drawer
         */
        boolean closeOnOutside = false;

        /**
         * 是否显示蒙层
         */
        boolean overlay = true;

        /**
         * 是否可通过拖拽改变 Drawer 大小
         */
        boolean resizable = false;

        /**
         * 容器的宽度，在 position 为 left 或 right 时生效
         */
        String width;

        /**
         * 容器的高度，在 position 为 top 或 bottom 时生效
         */
        String height;

        /**
         * 可以不设置，默认只有两个按钮。
         */
        List<Action> actions;

        /**
         * 支持 数据映射，如果不设定将默认将触发按钮的上下文中继承数据。
         */
        Map<String,Object> data;
    }
}
