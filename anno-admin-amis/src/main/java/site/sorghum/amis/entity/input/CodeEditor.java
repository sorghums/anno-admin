package site.sorghum.amis.entity.input;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 代码编辑器
 *
 * @author Sorghum
 * @since 2023/07/11
 */
@Data @EqualsAndHashCode(callSuper = true)
public class CodeEditor extends FormItem{
    {
        setType("editor");
    }


    /**
     * 语言
     */
    String language;

    /**
     * 高度
     * 编辑器高度，取值可以是 md、lg、xl、xxl
     */
    String size;

    /**
     * 是否显示全屏模式开关
     */
    Boolean allowFullscreen;

    /**
     * 其它配置
     * monaco 编辑器的其它配置，比如是否显示行号等，请参考这里，不过无法设置 readOnly，只读模式需要使用 disabled: true
     */
    Object options;

    /**
     * 占位描述
     * 没有值的时候展示
     */
    String placeholder;
}
