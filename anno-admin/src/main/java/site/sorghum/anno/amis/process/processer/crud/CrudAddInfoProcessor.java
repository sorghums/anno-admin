package site.sorghum.anno.amis.process.processer.crud;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.display.DialogButton;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.amis.model.CrudView;
import site.sorghum.anno.amis.process.BaseProcessor;
import site.sorghum.anno.amis.process.BaseProcessorChain;
import site.sorghum.anno.amis.util.AmisCommonUtil;
import site.sorghum.anno.anno.enums.AnnoDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CRUD视图初始化处理器
 *
 * @author Sorghum
 * @since 2023/07/07
 */
@Named
public class CrudAddInfoProcessor implements BaseProcessor {

    @Inject
    MetadataManager metadataManager;

    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        CrudView crudView = (CrudView) amisBaseWrapper.getAmisBase();
        // 判断是否可以编辑
        AnEntity anEntity = metadataManager.getEntity(clazz);
        List<AnField> anFields = anEntity.getFields();
        boolean canAdd = anFields.stream().anyMatch(AnField::isAddEnable);
        if (!canAdd) {
            chain.doProcessor(amisBaseWrapper, clazz, properties);
            return;
        }

        List<AmisBase> formItems = generateFormItems(anFields);
        Crud crudBody = crudView.getCrudBody();
        Form filter = crudBody.getFilter();
        List<Action> actions = filter.getActions();
        DialogButton dialogButton = createDialogButton(anEntity, formItems);
        actions.add(0, dialogButton);
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }

    private List<AmisBase> generateFormItems(List<AnField> anFields) {
        List<AmisBase> formItems = new ArrayList<>();
        for (AnField field : anFields) {
            if (field.isAddEnable()) {
                FormItem formItem = new FormItem();
                formItem.setName(field.getFieldName());
                formItem.setLabel(field.getTitle());
                // 单独设置label宽度
                formItem.setLabelWidth(StrUtil.length(formItem.getLabel()) * 14);
                formItem.setRequired(field.isEditNotNull());
                formItem.setPlaceholder(field.getEditPlaceHolder());
                formItem = AnnoDataType.editorExtraInfo(formItem, field);
                formItems.add(formItem);
            }
        }
        return formItems;
    }

    private DialogButton createDialogButton(AnEntity anEntity, List<AmisBase> formItems) {
        DialogButton dialogButton = new DialogButton();
        dialogButton.setLabel("新增");
        dialogButton.setIcon("fa fa-plus pull-left");
        dialogButton.setLevel("primary");
        dialogButton.setDialog(createAddDialog(anEntity, formItems));
        return dialogButton;
    }

    private DialogButton.Dialog createAddDialog(AnEntity anEntity, List<AmisBase> formItems) {
        return new DialogButton.Dialog() {{
            setTitle("新增");
            setBody(
                new Form() {{
                    setWrapWithPanel(false);
                    setApi(new Api() {{
                        setMethod("post");
                        setUrl("/amis/system/anno/${clazz}/save");
                    }});
                    setId("simple-add-form");
                    setSize("md");
                    setMode("horizontal");
                    setHorizontal(new FormHorizontal() {{
                        setRightFixed("sm");
                        setJustify(true);
                    }});
                    setBody(AmisCommonUtil.formItemToGroup(anEntity, formItems, 2));
                    setOnEvent(createSubmitEvent());
                }}
            );
        }};
    }

    private Map<String, Object> createSubmitEvent() {
        return new HashMap<>() {{
            put("submitSucc", new HashMap<>() {{
                put("actions",
                    CollUtil.newArrayList(new HashMap<>() {{
                                              put("actionType", "reload");
                                              put("componentId", "crud_template_main");
                                          }}
                    ));
            }});
        }};
    }
}
