package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

/**
 * 链接
 *
 * @author sorghum
 * @date 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Link extends AmisBase {
    {
        //如果在 Table、Card 和 List 中，为"link"；在 Form 中用作静态展示，为"static-link"
        setType("link");
    }

    /**
     * 标签内文本
     */
    String body;


    /**
     * 链接地址
     */
    String href;

    /**
     * 是否在新标签页打开
     */
    boolean blank = false;

    /**
     * a 标签的 target，优先于 blank 属性
     */
    String htmlTarget;

    /**
     * a 标签的 title
     */
    String title;

    /**
     * 禁用超链接
     */
    boolean disabled = false;

    /**
     * 超链接图标，以加强显示
     */
    String icon;

    /**
     * 右侧图标
     */
    String rightIcon;
}
