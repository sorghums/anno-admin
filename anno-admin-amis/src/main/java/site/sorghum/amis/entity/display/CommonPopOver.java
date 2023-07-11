package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

/**
 * 常见PopOver
 *
 * @author Sorghum
 * @since 2023/07/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommonPopOver extends AmisBase {
    {
        setType(null);
    }

    /**
     * 模式
     * popOver
     * dialog
     * drawer
     */
    String mode = "popOver";

    /**
     * 大小
     * sm md lg xl full
     */
    String size;

    /**
     * 位置
     * center left-top right-top left-bottom right-bottom atX-atY-myX-myY left-top-right-bottom left-center-right-center
     * ...
     * fixed-center fixed-left-top fixed-right-top fixed-left-bottom fixed-right-bottom
     */
    String position;

    /**
     * 偏移
     */
    Object offset;

    /**
     * 触发
     * click hover
     */
    String trigger;

    /**
     * 是否显示图标
     */
    Boolean showIcon;

    /**
     * 标题
     */
    String title;

    /**
     * 内容
     */
    AmisBase body;
}
