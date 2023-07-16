package site.sorghum.anno.modular.type.parser;

import cn.hutool.core.bean.BeanUtil;
import org.noear.solon.annotation.Component;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.CodePopOver;
import site.sorghum.amis.entity.display.CommonPopOver;
import site.sorghum.amis.entity.input.CodeEditor;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno.metadata.AnField;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.type.TypeParser;

import java.util.Map;

/**
 * 代码编辑器类型解析器
 *
 * @author Sorghum
 * @since 2023/07/11
 */
@Component
public class CodeEditorTypeParser implements TypeParser {
    @Override
    public Map<String, Object> parseDisplay(AmisBase amisBase, AnField anField) {
        CommonPopOver commonPopOver = new CommonPopOver();
        CodeEditor codeEditor = new CodeEditor(){{
            setDisabled(true);
        }};
        CodePopOver codePopOver = new CodePopOver();
        commonPopOver.setBody(codeEditor);
        BeanUtil.copyProperties(amisBase, codeEditor,"type");
        BeanUtil.copyProperties(amisBase, codePopOver,"type");
        codePopOver.setTpl("${" + codePopOver.getName() + "|truncate:12}");
        codePopOver.setPopOver(commonPopOver);
        return mergeObj(codePopOver, amisBase);
    }

    @Override
    public FormItem parseEdit(FormItem formItem, AnField anField) {
        CodeEditor codeEditor = new CodeEditor();
        BeanUtil.copyProperties(formItem, codeEditor);
        return codeEditor;
    }
}
