package site.sorghum.anno.amis.process.processer.crud;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.URLUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.display.DialogButton;
import site.sorghum.amis.entity.display.IFrame;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.layout.Tabs;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._metadata.AnColumnButton;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.amis.model.CrudView;
import site.sorghum.anno.amis.process.BaseProcessor;
import site.sorghum.anno.amis.process.BaseProcessorChain;
import site.sorghum.anno.amis.util.AmisCommonUtil;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.proxy.PermissionProxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * CRUD视图行编辑按钮处理器
 * CRUD Edit Button Processor
 */
@Named
public class CrudEditInfoProcessor implements BaseProcessor {

    @Inject
    MetadataManager metadataManager;

    @Inject
    PermissionProxy permissionProxy;

    private static final String EDIT_BUTTON_LABEL = "编辑";
    private static final String EDIT_DIALOG_TITLE = "编辑";
    private static final String OPERATION_COLUMN_LABEL = "操作";

    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        AnEntity entity = metadataManager.getEntity(clazz);

        CrudView crudView = (CrudView) amisBaseWrapper.getAmisBase();
        // 判断是否可以编辑
        List<AnField> fields = entity.getFields();
        boolean canEdit = fields.stream().anyMatch(AnField::isEditEnable);
        if (!canEdit) {
            chain.doProcessor(amisBaseWrapper, clazz, properties);
            return;
        }

        Crud crudBody = crudView.getCrudBody();
        List<Map> columns = crudBody.getColumns();

        Optional<Map> operationColumn = columns.stream()
            .filter(column -> OPERATION_COLUMN_LABEL.equals(MapUtil.getStr(column, "label")))
            .findFirst();

        if (operationColumn.isPresent()) {
            Object buttons = operationColumn.get().get("buttons");
            if (buttons instanceof List<?>) {
                List<Object> buttonList = (List<Object>) buttons;
                DialogButton dialogButton = createEditDialogButton(fields, entity);
                buttonList.add(dialogButton);
            }
        } else {
            throw new BizException("操作列不存在");
        }

        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }

    private DialogButton createEditDialogButton(List<AnField> fields, AnEntity entity) {
        DialogButton dialogButton = new DialogButton();
        dialogButton.setLabel(EDIT_BUTTON_LABEL);
        dialogButton.setSize("lg");

        ArrayList<AmisBase> formItems = new ArrayList<>();
        fields.forEach(field -> {
            FormItem formItem = createFormItem(field);
            if (!field.isEditEnable()) {
                formItem.setHidden(true);
            }
            formItems.add(formItem);
        });

        Tabs tabs = new Tabs();
        processEditTabs(entity, tabs);
        formItems.add(tabs);

        Api api = new Api();
        api.setMethod("post");
        api.setUrl("/amis/system/anno/${clazz}/updateById");

        Form form = new Form();
        form.setId("simple-edit-form");
        form.setWrapWithPanel(false);
        form.setApi(api);
        form.setMode("horizontal");
        form.setHorizontal(new Form.FormHorizontal() {{
            setRightFixed("sm");
            setJustify(true);
        }});
        form.setBody(AmisCommonUtil.formItemToGroup(entity, formItems, 2));

        DialogButton.Dialog dialog = new DialogButton.Dialog();
        dialog.setTitle(EDIT_DIALOG_TITLE);
        dialog.setBody(form);

        dialogButton.setDialog(dialog);

        return dialogButton;
    }

    private FormItem createFormItem(AnField field) {
        FormItem formItem = new FormItem();
        formItem.setName(field.getFieldName());
        formItem.setLabel(field.getTitle());
        formItem.setLabelWidth(formItem.getLabel().length() * 14);
        formItem.setRequired(field.isEditNotNull());
        formItem.setPlaceholder(field.getEditPlaceHolder());
        formItem = AnnoDataType.editorExtraInfo(formItem, field);
        return formItem;
    }

    private void processEditTabs(AnEntity anEntity, Tabs tabs) {
        List<AnColumnButton> anColumnButtons = anEntity.getColumnButtons();
        for (AnColumnButton anColumnButton : anColumnButtons) {
            if (!AmisCommonUtil.isPermissionGranted(permissionProxy,anColumnButton,anEntity)){
                continue;
            }
            Tabs.Tab tab = AmisCommonUtil.createTabForColumnButton(anColumnButton);
            tabs.getTabs().add(tab);
        }
    }
}
