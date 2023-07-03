package site.sorghum.amis.entity.input;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 输入密码
 *
 * @author sorghum
 * @since 2023/07/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InputPassword extends InputText{
    {
        setType("input-password");
    }

    /**
     * 是否显示密码切换按钮
     */
    Boolean revealPassword = true;
}
