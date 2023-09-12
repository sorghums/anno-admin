package site.sorghum.anno.amis.nprocess;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.Group;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno.anno.enums.AnnoDataType;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.collection.CollUtil.split;

public class BaseProcess {
    public String getTemplateContent(String templateName) {
        return ResourceUtil.readStr("classpath:amis/" + templateName + ".json", Charset.defaultCharset());
    }



    public List<AmisBase> createFilterFormItems(AnEntity anEntity) {
        List<AnField> fields = anEntity.getFields();
        List<AmisBase> formItems = fields.stream()
            .filter(AnField::isSearchEnable)
            .map(field -> {
                FormItem formItem = this.createFormItem(field);
                formItem.setRequired(false);
                return formItem;
            })
            .collect(Collectors.toList());

        List<AmisBase> body = new ArrayList<>();
        CollUtil.split(formItems, 4).forEach(columns -> {
            Group group = new Group() {{
                setBody(columns);
            }};
            body.add(group);
        });
        return body;
    }


    public FormItem createFormItem(AnField field) {
        FormItem formItem = new FormItem();
        formItem.setName(field.getFieldName());
        formItem.setLabel(field.getTitle());
        formItem.setLabelWidth(formItem.getLabel().length() * 14);
        formItem.setRequired(field.isEditNotNull());
        formItem.setPlaceholder(field.getEditPlaceHolder());
        formItem = AnnoDataType.editorExtraInfo(formItem, field);
        return formItem;
    }

}
