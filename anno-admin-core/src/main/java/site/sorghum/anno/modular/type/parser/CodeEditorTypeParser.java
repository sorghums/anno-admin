package site.sorghum.anno.modular.type.parser;

import cn.hutool.core.bean.BeanUtil;
import org.noear.snack.ONode;
import org.noear.solon.annotation.Component;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.Code;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.input.CodeEditor;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.modular.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.modular.type.TypeParser;

import java.util.ArrayList;
import java.util.Map;

@Component
public class CodeEditorTypeParser implements TypeParser {
    @Override
    public Map<String, Object> parseDisplay(AmisBase amisBase, AnnoField annoField) {
        Code code = new Code();
        return mergeObj(code, amisBase);
    }

    @Override
    public FormItem parseEdit(FormItem formItem, AnnoField annoField) {

        CodeEditor codeEditor = new CodeEditor();
        BeanUtil.copyProperties(formItem, codeEditor);
        return codeEditor;
    }
}
