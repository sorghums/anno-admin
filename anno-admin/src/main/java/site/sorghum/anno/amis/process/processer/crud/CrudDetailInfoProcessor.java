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
 * CRUD视图行详情按钮处理器
 *
 * @author Sorghum
 * @since 2023/09/07
 */
@Named
public class CrudDetailInfoProcessor implements BaseProcessor {

    @Inject
    MetadataManager metadataManager;

    @Inject
    PermissionProxy permissionProxy;

    private static final String DETAIL_BUTTON_LABEL = "详情";
    private static final String DETAIL_DIALOG_TITLE = "详情";
    private static final String OPERATION_COLUMN_LABEL = "操作";

    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        AnEntity entity = metadataManager.getEntity(clazz);
        CrudView crudView = (CrudView) amisBaseWrapper.getAmisBase();
        Crud crudBody = crudView.getCrudBody();
        List<Map> columns = crudBody.getColumns();
        Optional<Map> operationColumn = columns.stream()
            .filter(column -> OPERATION_COLUMN_LABEL.equals(column.get("label")))
            .findFirst();
        if (operationColumn.isPresent()) {
            Object buttons = operationColumn.get().get("buttons");
            if (buttons instanceof List<?>) {
                List<Object> buttonListMap = (List<Object>) buttons;
                DialogButton dialogButton = createDetailDialogButton(entity);
                buttonListMap.add(dialogButton);
            }
        } else {
            throw new BizException("操作列不存在");
        }
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }

    private DialogButton createDetailDialogButton(AnEntity anEntity) {
        List<AnField> fields = anEntity.getFields();
        DialogButton dialogButton = new DialogButton();
        dialogButton.setLabel(DETAIL_BUTTON_LABEL);
        dialogButton.setLevel("info");

        ArrayList<AmisBase> formItems = new ArrayList<>();
        fields.forEach(field -> {
            FormItem formItem = createFormItem(field);
            formItems.add(formItem);
        });

        Tabs tabs = new Tabs();
        processEditTabs(tabs, anEntity);
        formItems.add(tabs);

        dialogButton.setDialog(createDetailDialog(anEntity, formItems));

        return dialogButton;
    }

    private DialogButton.Dialog createDetailDialog(AnEntity anEntity, ArrayList<AmisBase> formItems) {
        return new DialogButton.Dialog() {{
            setSize("lg");
            setTitle(DETAIL_DIALOG_TITLE);
            setBody(createDetailForm(anEntity, formItems));
        }};
    }

    private Form createDetailForm(AnEntity anEntity, ArrayList<AmisBase> formItems) {
        Form form = new Form();
        form.setId("simple-detail-form");
        form.setWrapWithPanel(false);
        form.setMode("horizontal");
        form.setHorizontal(new Form.FormHorizontal() {{
            setRightFixed("sm");
            setJustify(true);
        }});
        form.setBody(AmisCommonUtil.formItemToGroup(anEntity, formItems, 2));
        form.setActions(new ArrayList<>());
        return form;
    }

    private FormItem createFormItem(AnField field) {
        FormItem formItem = new FormItem();
        formItem.setName(field.getFieldName());
        formItem.setLabel(field.getTitle());
        formItem.setLabelWidth(formItem.getLabel().length() * 14);
        formItem.setRequired(field.isEditNotNull());
        formItem.setPlaceholder(field.getEditPlaceHolder());
        formItem = AnnoDataType.editorExtraInfo(formItem, field);
        formItem.setDisabled(true);
        return formItem;
    }

    private void processEditTabs(Tabs tabs, AnEntity anEntity) {
        List<AnColumnButton> anColumnButtons = anEntity.getColumnButtons();
        for (AnColumnButton anColumnButton : anColumnButtons) {
            if (!AmisCommonUtil.isPermissionGranted(permissionProxy,anColumnButton, anEntity)) {
                continue;
            }

            Tabs.Tab tab = AmisCommonUtil.createTabForColumnButton(anColumnButton);
            tabs.getTabs().add(tab);
        }
    }
}
