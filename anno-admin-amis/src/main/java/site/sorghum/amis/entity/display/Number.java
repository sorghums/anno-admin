package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

/**
 * 数量
 *
 * @author sorghum
 * @date 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Number extends AmisBase {
    {
        //如果在 Table、Card 和 List 中，为"number"；在 Form 中用作静态展示，为"static-number" 或者 input-number 配置 static 属性
        setType("number");
    }
    /**
     * 数值
     */
    String value;

    /**
     * 在其他组件中，时，用作变量映射
     */
    String name;

    /**
     * 占位内容
     */
    String placeholder;

    /**
     * 是否千分位展示
     */
    Boolean kilobitSeparator = true;

    /**
     * 用来控制小数点位数
     */
    Integer precision;

    /**
     * 是否用百分比展示，如果是数字，还可以控制百分比小数点位数
     */
    Boolean percent;

    /**
     * 前缀
     */
    String prefix;

    /**
     * 后缀
     */
    String affix;

}
