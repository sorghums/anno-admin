package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

/**
 * 备注
 *
 * @author sorghum
 * @date 2023/06/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Remark extends AmisBase {

    {
        setType("type");
    }

    /**
     * 提示文本
     */
    String content;

    /**
     * 弹出位置
     */
    String placement;

    /**
     * 触发条件
     * ['hover', 'focus']
     */
    String trigger;

    /**
     * 图标
     */
    String icon = "fa fa-question-circle";

    /**
     * 图标样式
     * 'circle' | 'square'
     */
    String shape = "circle";

}
