package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Picker extends FormItem{
    {
        setType("picker");
    }

    /**
     * 	数据
     */
    List<Options.Option> options;

    /**
     * 	string或 API 或 数据映射
     */
    Object source;

    /**
     * 	是否为多选。
     */
    Boolean multiple;

    /**
     * 	是否拼接符
     */
    Boolean delimiter;

    /**
     * 	选项标签字段
     */
    String labelField;

    /**
     * 	选项值字段
     */
    String valueField;

    /**
     * 	拼接值
     */
    String joinValues;

    /**
     * 	提取值
     */
    String extractValue;

    /**
     * 	自动填充
     */
    Object autoFill;


    /**
     * 	设置模态框的标题
     */
    String modalTitle;

    /**
     * 	设置 dialog 或者 drawer，用来配置弹出方式。
     */
    String modalMode;

    /**
     * 	即用 List 类型的渲染，来展示列表信息。更多配置参考 CRUD
     */
    String pickerSchema;

    /**
     * 	是否使用内嵌模式
     */
    Boolean embed;




}
