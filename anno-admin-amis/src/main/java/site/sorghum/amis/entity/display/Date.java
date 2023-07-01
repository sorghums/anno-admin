package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

/**
 * 日期
 *
 * @author sorghum
 * @date 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Date extends AmisBase {
    {
        //如果在 Table、Card 和 List 中，为"date"；在 Form 中用作静态展示，为"static-date"
        setType("date");
    }
    /**
     * 显示的日期数值
     */
    String  value;

    /**
     * 在其他组件中，时，用作变量映射
     */
    String  name;

    /**
     * 占位内容
     */
    String  placeholder;

    /**
     * 展示格式, 更多格式类型请参考 文档
     */
    String  format = "YYYY-MM-DD";

    /**
     * 数据格式，默认为时间戳。更多格式类型请参考 文档
     */
    String  valueFormat = "X";

    /**
     * 是否显示相对当前的时间描述，比如: 11 小时前、3 天前、1 年前等，fromNow 为 true 时，format 不生效。
     */
    Boolean fromNow = false;

    /**
     * 更新频率， 默认为 1 分钟
     */
    Integer updateFrequency = 60000;
}
