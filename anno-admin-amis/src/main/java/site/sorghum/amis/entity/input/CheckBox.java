package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 勾选框
 *
 * @author sorghum
 * @since 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CheckBox extends FormItem{
    {
        setType("checkbox");
    }

    /**
     * 选项说明,类似label
     */
    String option;

    /**
     * 标识真值
     */
    Object value;

    /**
     * 标识假值
     */
    Object falseValue = false;

    /**
     * 设置 option 类型
     * default｜button
     */
    String optionType = "default";
}
