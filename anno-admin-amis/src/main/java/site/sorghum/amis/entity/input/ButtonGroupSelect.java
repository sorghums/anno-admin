package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.function.Api;

import java.util.List;

/**
 * 按钮组选择
 *
 * @author sorghum
 * @date 2023/06/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ButtonGroupSelect extends FormItem{
    {
        setType("button-group-select");
    }

    /**
     * 是否使用垂直模式
     */
    private Boolean vertical = false;

    /**
     * 是否使用平铺模式
     */
    private Boolean tiled = false;

    /**
     * 按钮样式
     * 'link' | 'primary' | 'secondary' | 'info'|'success' | 'warning' | 'danger' | 'light'| 'dark' | 'default'
     */
    private String btnLevel = "default";

    /**
     * 选中按钮样式
     * 'link' | 'primary' | 'secondary' | 'info'|'success' | 'warning' | 'danger' | 'light'| 'dark' | 'default'
     */
    private String activeBtnLevel = "default";


    /**
     * 	选项组
     */
    private List<Options.Option> options;


    /**
     * API 或 数据映射
     * 选项组源，可通过数据映射获取当前数据域变量、或者配置 API 对象
     */
    Api source;

    /**
     * 是否支持多选
     */
    Boolean multiple;


    /**
     * 是否拼接value值
     */
    Boolean joinValues = true;

    /**
     * 是否将value值抽取出来组成新的数组，只有在joinValues是false是生效
     */
    Boolean extractValue = false;


}
