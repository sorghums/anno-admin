package site.sorghum.anno.modular.amis.process.processer.crudm2m;

import cn.hutool.core.map.MapUtil;
import org.noear.solon.annotation.Component;
import org.noear.wood.annotation.PrimaryKey;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.display.DialogButton;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno.common.exception.BizException;
import site.sorghum.anno.modular.amis.model.CrudM2mView;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;
import site.sorghum.anno.modular.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.anno.util.AnnoUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * crud-m2m编辑信息处理器
 *
 * @author Sorghum
 * @since 2023/07/10
 */
@Component
public class CrudM2mEditInfoProcessor implements BaseProcessor {
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        CrudM2mView crudM2mView = (CrudM2mView) amisBaseWrapper.getAmisBase();
        // 判断是否可以编辑
        List<Field> annoFields = AnnoUtil.getAnnoFields(clazz);
        boolean canEdit = annoFields.stream().map(f -> f.getAnnotation(AnnoField.class)).anyMatch(annoField -> annoField.edit().editEnable());
        if (!canEdit) {
            return;
        }
        Crud crudBody = crudM2mView.getCrudBody();
        List<Map> columns = crudBody.getColumns();
        Map columnJson = columns.stream().filter(column -> "操作".equals(MapUtil.getStr(column, "label"))).findFirst().orElseThrow(
                () -> new BizException("操作列不存在")
        );
        Object buttons = columnJson.get("buttons");
        if (buttons instanceof List<?> buttonList) {
            List<Object> buttonListMap = (List<Object>) buttonList;
            DialogButton dialogButton = new DialogButton();
            dialogButton.setLabel("编辑");
            ArrayList<AmisBase> formItems = new ArrayList<>() {{
                List<Field> fields = AnnoUtil.getAnnoFields(clazz);
                for (Field field : fields) {
                    AnnoField annoField = field.getAnnotation(AnnoField.class);
                    PrimaryKey annoId = field.getAnnotation(PrimaryKey.class);
                    if (annoId != null) {
                        add(new FormItem() {{
                            setName(field.getName());
                            setType("hidden");
                        }});
                        continue;
                    }
                    AnnoEdit edit = annoField.edit();
                    if (edit.editEnable()) {
                        FormItem formItem = new FormItem();
                        formItem.setName(field.getName());
                        formItem.setLabel(annoField.title());
                        formItem.setRequired(edit.notNull());
                        formItem.setPlaceholder(edit.placeHolder());
                        formItem = AnnoDataType.editorExtraInfo(formItem, annoField);
                        add(formItem);
                    }
                }
            }};
            dialogButton.setDialog(
                    new DialogButton.Dialog() {{
                        setTitle("编辑");
                        setBody(
                                new Form() {{
                                    setId("simple-edit-form");
                                    setWrapWithPanel(false);
                                    setApi(new Api() {{
                                        setMethod("post");
                                        setUrl("/system/anno/${clazz}/updateById");
                                    }});
                                    setBody(formItems);
                                }}
                        );
                    }}
            );
            buttonListMap.add(dialogButton);
        }
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
