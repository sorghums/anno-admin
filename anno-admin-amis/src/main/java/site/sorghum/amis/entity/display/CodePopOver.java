package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

/**
 * CodePopOver
 *
 * @author Sorghum
 * @since 2023/07/11
 */
@Data @EqualsAndHashCode(callSuper = true)
public class CodePopOver extends AmisBase {
    {
        setType("tpl");
    }

    /**
     * 变量名
     */
    String name;

    /**
     * 标签
     */
    String label;

    /**
     * 模板
     */
    String tpl;

    /**
     * 弹出框
     */
    Object popOver;
}
