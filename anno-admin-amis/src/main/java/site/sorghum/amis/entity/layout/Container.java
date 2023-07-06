package site.sorghum.amis.entity.layout;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.function.Api;

import java.util.List;

/**
 * 普通容器
 *
 * @author Sorghum
 * @since 2023/07/05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Container extends AmisBase {
    {
        setType("container");
    }


    /**
     * 往页面的内容区域加内容
     */
    List<AmisBase> body;

    /**
     * Body dom 类名
     */
    String bodyClassName = "wrapper";

    /**
     * Body dom 样式
     */
    String wrapperComponent = "div";

    /**
     * 自定义样式
     */
    Object style;

}
