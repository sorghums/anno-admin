package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InputNumber extends FormItem{
    {
        setType("input-number");
    }

    /**
     * 最小值
     */
    Integer min;

    /**
     * 最大值
     */
    Integer max;

    /**
     * 步长
     */
    Integer step;

    /**
     * 精度，即小数点后几位，支持 0 和正整数
     */
    Integer precision;

    /**
     * 是否显示上下点击按钮
     */
    Boolean showSteps;

    /**
     * 前缀
     */
    String prefix;

    /**
     * 后缀
     */
    String suffix;

    /**
     * 千分分隔
     */
    Boolean kilobitSeparator;

    /**
     * 键盘事件（方向上下）
     */
    Boolean keyboard;

    /**
     * 是否使用大数
     */
    Boolean big;

    /**
     * 样式类型
     */
    String displayMode;

    /**
     * 清空输入内容时，组件值将设置为resetValue
     */
    String resetValue;
}
