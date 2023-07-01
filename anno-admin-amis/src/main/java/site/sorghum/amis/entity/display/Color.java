package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

/**
 * 颜色
 *
 * @author sorghum
 * @date 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Color extends AmisBase {
    {
        //如果在 Table、Card 和 List 中，为"color"；在 Form 中用作静态展示，为"static-color"
        setType("color");
    }
    /**
     * 显示的颜色值
     */
    String value;

    /**
     * 在其他组件中，时，用作变量映射
     */
    String name;

    /**
     * 默认颜色值
     */
    String defaultColor = "#ccc";

    /**
     * 是否显示右边的颜色值
     */
    Boolean showValue = true;

}
