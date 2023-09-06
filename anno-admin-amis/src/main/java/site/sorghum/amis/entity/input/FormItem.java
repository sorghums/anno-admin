package site.sorghum.amis.entity.input;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.Remark;

import java.util.Map;

/**
 * 表单项
 *
 * @author sorghum
 * @since 2023/06/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FormItem extends AmisBase {
    /**
     * 表单控制器类名
     */
    String inputClassName;

    /**
     * label 的类名
     */
    String labelClassName;

    /**
     * 字段名，指定该表单项提交时的 key
     */
    private String name;

    /**
     * 表单默认值
     */
    private Object value;

    /**
     * 表单项标签
     */
    String label;

    /**
     * 表单项标签对齐方式，默认右对齐，仅在 mode为horizontal 时生效
     */
    private String labelAlign;

    /**
     * 表单项标签描述
     */
    private Remark labelRemark;

    /**
     * 表单项描述
     */
    private String description;

    /**
     * 表单项描述
     */
    private String placeholder;

    /**
     * 是否为 内联 模式
     */
    private Boolean inline;

    /**
     * 是否该表单项值发生变化时就提交当前表单。
     */
    private Boolean autoSubmit;

    /**
     * 当前表单项是否是禁用状态
     */
    private Boolean disabled;

    /**
     * 当前表单项是否禁用的条件
     */
    private String disabledOn;

    /**
     * 当前表单项是否可见
     */
    private Boolean visible;
    /**
     * 当前表单项是否可见的条件
     */
    private String visibleOn;

    /**
     * 当前表单项是否必填
     */
    private Boolean required;

    /**
     * 通过表达式来配置当前表单项是否为必填。
     */
    private String requiredOn;

    /**
     * 表单项值格式验证，支持设置多个，多个规则用英文逗号隔开。
     */
    private String validations;

    /**
     * 表单校验接口
     */
    private String validateApi;

    /**
     * 数据录入配置，自动填充或者参照录入
     */
    private Object autoFill;

    /**
     * 整个表单静态方式展示(仅作展示态)
     */
    @JSONField(name = "static")
    Boolean bStatic;

    /**
     * 表单静态展示时使用的类名
     */
    String staticClassName;

    /**
     * 静态展示时的 Label 的类名
     */
    String staticLabelClassName;

    /**
     * 静态展示时的 value 的类名
     */
    String staticInputClassName;

    /**
     * 自定义静态展示方式
     */
    Map<String, Object> staticSchema;

    /**
     * select、checkboxes 等选择类组件多选时展示态展示的数量
     */
    Integer staticItemCount;

    /**
     * 表单项大小
     */
    String size;

    /**
     * 为空时是否清空值
     */
    Boolean clearValueOnEmpty;

    /**
     * 表单项的宽度
     */
    String columnRatio;

    @Data
    public static class AutoFill {

        /**
         * true 为参照录入，false 自动填充
         */
        Boolean showSuggestion;

        /**
         * 自动填充接口/参照录入筛选 CRUD 请求配置
         */
        String api;

        /**
         * 是否展示数据格式错误提示，默认为 true
         */
        Boolean silent;

        /**
         * 自动填充/参照录入数据映射配置，键值对形式，值支持变量获取及表达式
         */
        Map<String, Object> fillMapping;

        /**
         * showSuggestion 为 true 时，参照录入支持的触发方式，目前支持 change「值变化」｜ focus 「表单项聚焦」
         */
        String trigger;

        /**
         * showSuggestion 为 true 时，参照弹出方式 dialog, drawer, popOver
         */
        String mode;

        /**
         * showSuggestion 为 true 时，设置弹出 dialog,drawer,popOver 中 picker 的 labelField
         */
        String labelField;

        /**
         * showSuggestion 为 true 时，参照录入 mode 为 popOver 时，可配置弹出位置
         */
        String position;

        /**
         * showSuggestion 为 true 时，参照录入 mode 为 dialog 时，可设置大小
         */
        String size;

        /**
         * showSuggestion 为 true 时，数据展示列配置
         */
        String columns;

        /**
         * showSuggestion 为 true 时，数据查询过滤条件
         */
        String filter;
    }
}
