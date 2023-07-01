package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 城市选择器
 *
 * @author sorghum
 * @date 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class City extends FormItem {
    {
        setType("city");
    }

    /**
     * 允许选择城市
     */
    Boolean allowCity = true;

    /**
     * 允许选择区县
     */
    Boolean allowDistrict = true;

    /**
     * 允许搜索
     */
    Boolean searchable = false;

    /**
     * 默认 true 是否抽取值
     * 如果设置成 false 值格式会变成对象，包含 code、province、city 和 district 文字信息。
     */
    Boolean extractValue = true;
}
