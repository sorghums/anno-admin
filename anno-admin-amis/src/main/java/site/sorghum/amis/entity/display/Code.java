package site.sorghum.amis.entity.display;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.AmisBase;

/**
 * 代码
 *
 * @author Sorghum
 * @since 2023/07/11
 */
@Data @EqualsAndHashCode(callSuper = true)
public class Code extends AmisBase {
    {
        setType("code");
    }

    /**
     * 显示的值
     */
    String value;

    /**
     * 在其他组件中，时，用作变量映射
     */
    String name;

    /**
     * 所使用的高亮语言，默认是 plaintext
     */
    String language;

    /**
     * 默认 tab 大小
     * 4	默认 tab 大小
     */
    Integer tabSize;

    /**
     * 主题
     * 'vs'	主题，还有 'vs-dark'
     */
    String editorTheme;

    /**
     * 是否折行
     * true	是否折行
     */
    Boolean wordWrap;

}
