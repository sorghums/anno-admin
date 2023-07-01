package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.function.Api;

import java.util.List;

/**
 * 复选框
 *
 * @author sorghum
 * @date 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CheckBoxes extends FormItem {
    {
        setType("checkboxes");
    }

    /**
     * 选项组
     */
    List<Options.Option> options;
    /**
     * API
     */
    Api source;

    /**
     * 拼接符
     */
    String delimiter = ",";

    /**
     * 是否拼接value值
     */
    Boolean joinValues = true;

    /**
     * 是否将value值抽取出来组成新的数组，只有在joinValues是false是生效
     */
    Boolean extractValue = false;

    /**
     * 选项按几列显示，默认为一列
     */
    Integer columnsCount = 1;

    /**
     * 支持自定义选项渲染
     */
    String menuTpl;

    /**
     * 是否支持全选
     */
    Boolean checkAll = false;

    /**
     * 是否显示为一行
     */
    Boolean inline = true;

    /**
     * 默认是否全选
     */
    Boolean defaultCheckAll = false;

    /**
     * 是否可创建新的选项
     */
    Boolean creatable = false;

    /**
     * 创建新选项的按钮文本
     */
    String createBtnLabel = "新增选项";

    /**
     * 	自定义新增表单项
     */
    List<FormItem> addControls;

    /**
     * 配置新增选项接口
     */
    Api addApi;

    /**
     * 是否可创建编辑选项的组建
     */
    Boolean editable = false;

    /**
     * 	自定义编辑表单项
     */
    List<FormItem> editControls;

    /**
     * 配置编辑选项接口
     */
    Api editApi;

    /**
     * 是否可删除选项
     */
    Boolean removable = false;

    /**
     * 配置删除选项接口
     */
    Api deleteApi;

    /**
     * default | button
     */
    String optionType = "default";

    /**
     * 选项样式类名
     */
    String itemClassName;

    /**
     * 选项标签样式类名
     */
    String labelClassName;

}
