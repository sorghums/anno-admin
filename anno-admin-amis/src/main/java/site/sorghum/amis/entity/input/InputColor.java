package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;

/**
 * 输入颜色
 *
 * @author sorghum
 * @date 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InputColor extends FormItem{
    {
        setType("input-color");
    }
    /**
     * 请选择 hex、hls、rgb或者rgba。
     */
    String format = "hex";

    /**
     * 选择器底部的默认颜色，数组内为空则不显示默认颜色
     */
    List<String> presetColors = Arrays.asList("#ff0000", "#00ff00", "#0000ff");

    /**
     * 为false时只能选择颜色，使用 presetColors 设定颜色选择范围
     */
    Boolean enableAlpha = true;

    /**
     * 是否显示清除按钮
     */
    Boolean clearable = true;

    /**
     * 清除后，表单项值调整成该值
     */
    String clearValue = "";
}
