package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 输入日期时间
 *
 * @author sorghum
 * @since 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InputDatetime extends InputDate{
    {
        setType("input-datetime");
    }

    /**
     * 控制输入范围
     */
    InputTime.TimeConstraints timeConstraints;
}
