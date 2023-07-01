package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.function.Api;

import java.util.List;
import java.util.Map;

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
    //placeholder	string		占位文本
    //map	object或Array<object>		映射配置
    //source	string or API		API 或 数据映射
    //注：配置后映射值无法作为schema组件渲染
    //itemSchema	string或SchemaNode		2.5.2 自定义渲染模板，支持html或schemaNode；
    //当映射值是非object时，可使用${item}获取映射值；
    //当映射值是object时，可使用映射语法: ${xxx}获取object的值；
    //也可使用数据映射语法：${xxx}获取数据域中变量值。

    /**
     * 占位文本
     */
    String placeholder;

    /**
     * 映射配置
     */
    List<Map<String,Object>> map;

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
