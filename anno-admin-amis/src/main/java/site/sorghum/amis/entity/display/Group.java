package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.input.FormItem;

import java.util.List;

/**
 * 组
 *
 * @author sorghum
 * @since 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Group extends FormItem {
    {
        setType("group");
    }
    /**
     * 表单项集合
     */
    List<AmisBase> body;

    /**
     * 展示默认，同 Form 中的模式
     */
    String mode;

    /**
     * 表单项之间的间距，可选：xs、sm、normal
     */
    String gap = "normal";

    /**
     * 可以配置水平展示还是垂直展示。对应的配置项分别是：vertical、horizontal
     */
    String direction = "horizontal";
}
