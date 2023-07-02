package site.sorghum.amis.entity.function;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

import java.util.List;

/**
 * 按钮组
 *
 * @author sorghum
 * @date 2023/06/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ButtonGroup extends AmisBase {
    {
        setType("button-group");
    }

    /**
     * 是否使用垂直模式
     */
    private Boolean vertical = false;

    /**
     * 是否使用平铺模式
     */
    private Boolean tiled = false;

    /**
     * 按钮样式
     * 'link' | 'primary' | 'secondary' | 'info'|'success' | 'warning' | 'danger' | 'light'| 'dark' | 'default'
     */
    private String btnLevel = "default";

    /**
     * 选中按钮样式
     * 'link' | 'primary' | 'secondary' | 'info'|'success' | 'warning' | 'danger' | 'light'| 'dark' | 'default'
     */
    private String activeBtnLevel = "default";

    /**
     * 按钮
     */
    List<Action> buttons;

}
