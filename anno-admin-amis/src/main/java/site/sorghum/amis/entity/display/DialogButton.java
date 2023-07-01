package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.function.Action;

import java.util.Map;

/**
 * 对话框
 *
 * @author sorghum
 * @date 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DialogButton extends Action {

    {
        setActionType("dialog");
    }

    /**
     * 对话框
     */
    Dialog dialog;

    /**
     * 对话框
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Dialog extends AmisBase {
        {
            setType("dialog");
        }

        /**
         * 弹出层标题
         */
        String title;
        /**
         * 往 Dialog 内容区加内容
         */
        AmisBase body;
        /**
         * 指定 dialog 大小，支持: xs、sm、md、lg、xl、full
         */
        String size;

        /**
         * Dialog body 区域的样式类名
         */
        String bodyClassName;

        /**
         * 是否支持按 Esc 关闭 Dialog
         */
        boolean closeOnEsc = false;

        /**
         * 是否显示右上角的关闭按钮
         */
        boolean showCloseButton = true;

        /**
         * 是否在弹框左下角显示报错信息
         */
        boolean showErrorMsg = true;

        /**
         * 是否在弹框左下角显示 loading 动画
         */
        boolean showLoading = true;

        /**
         * 如果设置此属性，则该 Dialog 只读没有提交操作。
         */
        boolean disabled = false;

        /**
         * 如果想不显示底部按钮，可以配置：[]
         */
        Action[] actions;

        /**
         * 支持数据映射，如果不设定将默认将触发按钮的上下文中继承数据。
         */
        Map<String, Object> data;
    }
}
