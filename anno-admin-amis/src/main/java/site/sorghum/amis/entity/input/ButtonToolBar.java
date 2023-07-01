package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.function.Action;

import java.util.List;

/**
 * 按钮工具栏
 *
 * @author sorghum
 * @date 2023/06/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ButtonToolBar extends FormItem{
    {
        setType("button-toolbar");
    }

    List<Action> buttons;
}
