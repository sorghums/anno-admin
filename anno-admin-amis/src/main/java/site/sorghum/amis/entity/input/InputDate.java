package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 输入日期
 *
 * @author sorghum
 * @since 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InputDate extends FormItem{
    {
        setType("input-date");
    }

    /**
     * 默认值
     */
    String value;

    /**
     * 日期选择器值格式，更多格式类型请参考 文档
     */
    String format = "X";

    /**
     * 日期选择器显示格式，即时间戳格式，更多格式类型请参考 文档
     */
    String inputFormat = "YYYY-MM-DD";

    /**
     * 点选日期后，是否马上关闭选择框
     */
    Boolean closeOnSelect = false;

    /**
     * 占位文本
     */
    String placeholder;

    /**
     * 日期快捷键
     */
    String shortcuts;

    /**
     * 限制最小日期
     */
    String minDate;

    /**
     * 限制最大日期
     */
    String maxDate;

    /**
     * 保存 utc 值
     */
    Boolean utc = false;

    /**
     * 是否可清除
     */
    Boolean clearable = true;

    /**
     * 是否内联模式
     */
    Boolean embed = false;
}
