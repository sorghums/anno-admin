package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.function.Api;

import java.util.List;
import java.util.Map;

/**
 * 选择器表单项
 *
 * @author sorghum
 * @since 2023/06/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Options extends FormItem{
    {
        setType("select");
    }

    /**
     * 选项组
     */
    List<Option> options;

    /**
     * API 或 数据映射
     * 选项组源，可通过数据映射获取当前数据域变量、或者配置 API 对象
     */
    Api source;

    /**
     * 是否支持多选
     */
    Boolean multiple = false;

    /**
     * 是否拼接value值
     */
    Boolean joinValues = true;

    /**
     * 是否将value值抽取出来组成新的数组，只有在joinValues是false是生效
     */
    Boolean extractValue = false;

    /**
     * 每个选项的高度，用于虚拟渲染
     */
    Integer itemHeight = 32;

    /**
     * 在选项数量超过多少时开启虚拟渲染
     */
    Integer virtualThreshold = 100;

    /**
     * 默认情况下多选所有选项都会显示，通过这个可以最多显示一行，超出的部分变成 ...
     */
    Boolean valuesNoWrap = false;

    @Data
    public static class Option {
        /**
         * ID (可忽略)
         */
        String id;
        /**
         * 选项标签
         */
        String label;

        /**
         * 选项值
         */
        Object value;

        /**
         * 角标
         */
        Map<String , Object> badge;

        /**
         * 子选项
         */
        List<Option> children;
    }
}
