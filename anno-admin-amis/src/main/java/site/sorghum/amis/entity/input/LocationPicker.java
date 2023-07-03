package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 位置选择器
 *
 * @author sorghum
 * @since 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LocationPicker extends FormItem{
    {
        setType("location-picker");
    }

    /**
     * 地图厂商，目前只实现了百度地图和高德地图
     * 'baidu' | 'gaode'
     */
    String vendor = "baidu";

    /**
     * 百度/高德地图的 ak
     */
    String ak;

    /**
     * 输入框是否可清空
     */
    Boolean clearable = false;

    /**
     * 默认提示
     */
    String placeholder = "请选择位置";

    /**
     * 默为百度/高德坐标，可设置为'gcj02', 高德地图不支持坐标转换
     */
    String coordinatesType = "bd09";
}
