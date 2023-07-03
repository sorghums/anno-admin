package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.function.Api;

import java.util.List;

/**
 * 输入文本
 *
 * @author sorghum
 * @since 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InputText extends FormItem {
    {
        setType("input-text");
    }

    /**
     * 选项组
     */
    List<Options.Option> options;

    /**
     * 动态选项组
     */
    Api source;

    /**
     * 自动补全
     */
    Api autoComplete;

    /**
     * 是否多选
     */
    Boolean multiple;

    /**
     * 拼接符
     */
    String delimiter = ",";

    /**
     * 选项标签字段
     */
    String labelField = "label";

    /**
     * 选项值字段
     */
    String valueField = "value";

    /**
     * 拼接值
     */
    Boolean joinValues = true;

    /**
     * 提取值
     */
    Boolean extractValue = false;

    /**
     * 输入框附加组件，比如附带一个提示文字，或者附带一个提交按钮。
     */
    AddOn addOn;


    /**
     * 是否去除首尾空白文本。
     */
    Boolean trimContents;

    /**
     * 文本内容为空时去掉这个值
     */
    Boolean clearValueOnEmpty;

    /**
     * 是否可以创建，默认为可以，除非设置为 false 即只能选择选项中的值
     */
    Boolean creatable;

    /**
     * 是否可清除
     */
    Boolean clearable;

    /**
     * 清除后设置此配置项给定的值。
     */
    String resetValue;

    /**
     * 前缀
     */
    String prefix;

    /**
     * 后缀
     */
    String suffix;

    /**
     * 是否显示计数器
     */
    Boolean showCounter;

    /**
     * 限制最小字数
     */
    Integer minLength;


    /**
     * 限制最大字数
     */
    Integer maxLength;

    /**
     * 自动转换值，可选 transform: { lowerCase: true, upperCase: true }
     */
    Transform transform;

    /**
     * 输入框边框模式，全边框，还是半边框，或者没边框。
     * full | half | none
     */
    String borderMode = "full";


    /**
     * control 节点的 CSS 类名
     */
    String inputControlClassName;

    /**
     * 原生 input 标签的 CSS 类名
     */
    String nativeInputClassName;

    @Data
    public static class AddOn {
        /**
         * 请选择 text 、button 或者 submit。
         */
        String type;

        /**
         * 文字说明
         */
        String label;

        /**
         * addOn 位置
         */
        String position = "right";

        /**
         * 其他参数请参考按钮文档
         */
        String xxx;
    }


    @Data
    public static class Transform {
        /**
         * 转换为小写
         */
        Boolean lowerCase;

        /**
         * 转换为大写
         */
        Boolean upperCase;
    }

}
