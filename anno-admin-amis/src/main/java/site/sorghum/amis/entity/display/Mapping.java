package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.function.Api;

/**
 * 映射
 *
 * @author sorghum
 * @date 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Mapping extends AmisBase {
    {
        setType("mapping");
    }
    /**
     * 占位文本
     */
    String placeholder;

    /**
     * 映射配置
     */
    Object map;

    /**
     * API 或 数据映射
     */
    Api source;

    /**
     * 自定义渲染模板，支持html或schemaNode；
     * 当映射值是非object时，可使用${item}获取映射值；
     * 当映射值是object时，可使用映射语法: ${xxx}获取object的值；
     * 也可使用数据映射语法：${xxx}获取数据域中变量值。
     */
    String itemSchema;
}
