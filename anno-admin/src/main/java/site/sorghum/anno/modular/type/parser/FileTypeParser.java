package site.sorghum.anno.modular.type.parser;

import cn.hutool.core.bean.BeanUtil;
import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.Link;
import site.sorghum.amis.entity.display.Table;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.input.InputFile;
import site.sorghum.anno.metadata.AnField;
import site.sorghum.anno.modular.type.TypeParser;

import java.util.Map;

/**
 * 默认类型解析器
 *
 * @author Sorghum
 * @since 2023/07/07
 */
@Named
public class FileTypeParser implements TypeParser {
    @Override
    public Map<String, Object> parseDisplay(AmisBase amisBase, AnField anField) {
        Table.Column column = (Table.Column) amisBase;
        Link link = new Link();
        link.setType("static-link");
        link.setBody("文件地址");
        link.setHref("${" + column.getName() + "}");
        link.setBlank(true);
        return mergeObj(link, amisBase);
    }

    @Override
    public FormItem parseEdit(FormItem formItem, AnField anField) {
        InputFile inputFile = new InputFile();
        BeanUtil.copyProperties(formItem, inputFile, "type");
        return inputFile;
    }
}
