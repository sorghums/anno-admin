package site.sorghum.amis.entity.function;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

import java.util.Map;

/**
 * Anno专属Amis
 *
 * @author Sorghum
 * @since 2023/07/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AnAmis extends AmisBase {
    {
        setType("an-amis");
    }

    /**
     * 渲染目标类
     */
    String targetClass;

    /**
     * 请求参数
     */
    Map<String, Object> param;

    /**
     * 默认属性，用于覆盖AmisBase中的属性
     */
    Map<String, Object> defaultProps;
}
