package site.sorghum.anno.amis.nprocess;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.DialogButton;
import site.sorghum.amis.entity.display.Group;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.layout.Tabs;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.util.CryptoUtil;
import site.sorghum.anno._metadata.AnColumnButton;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.AnButton;
import site.sorghum.anno.amis.util.AmisCommonUtil;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.proxy.PermissionProxy;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BaseProcess {
    public String getTemplateContent(String templateName) {
        return ResourceUtil.readStr("classpath:amis/" + templateName + ".json", Charset.defaultCharset());
    }



    public DialogButton createDetailDialogButton(List<AnField> fields, AnEntity entity) {
        DialogButton dialogButton = new DialogButton();
        dialogButton.setLabel("详情");

        ArrayList<AmisBase> formItems = new ArrayList<>();
        fields.forEach(field -> {
            FormItem formItem = createFormItem(field);
            formItem.setDisabled(true);
            formItems.add(formItem);
        });

        Tabs tabs = new Tabs();
        processEditTabs(entity, tabs);
        formItems.add(tabs);


        Form form = new Form();
        form.setId("simple-edit-form");
        form.setWrapWithPanel(false);
        form.setMode("horizontal");
        form.setHorizontal(new Form.FormHorizontal() {{
            setRightFixed("sm");
            setJustify(true);
        }});
        form.setBody(AmisCommonUtil.formItemToGroup(entity, formItems, 2));

        DialogButton.Dialog dialog = new DialogButton.Dialog();
        dialog.setTitle("详情");
        dialog.setBody(form);

        dialogButton.setDialog(dialog);

        return dialogButton;
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

    public Action createDeleteAction(Class<?> clazz) {
        Action delete = new Action();
        delete.setActionType("ajax");
        delete.setLabel("删除");
        delete.setLevel("danger");
        delete.setConfirmText("您确认要删除?");
        delete.setApi(createDeleteApi(clazz));
        return delete;
    }

    public Api createDeleteApi(Class<?> clazz) {
        Api api = new Api();
        api.setMethod("post");
        api.setUrl("/amis/system/anno/" + clazz.getSimpleName() + "/removeById");
        return api;
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


    public Action createTableAction(AnButton anButton) {
        Action action = new Action();
        if (StrUtil.isNotBlank(anButton.getJumpUrl())) {
            action.setLabel(anButton.getName());
            action.setActionType("url");
            action.setUrl(anButton.getJumpUrl());
        } else if (StrUtil.isNotBlank(anButton.getJsCmd())) {
            action.setLabel(anButton.getName());
            action.setOnClick(anButton.getJsCmd());
        } else if (anButton.isJavaCmdEnable()) {
            action.setLabel(anButton.getName());
            action.setActionType("ajax");
            action.setApi(
                new Api() {{
                    setMethod("post");
                    setUrl("/amis/system/anno/runJavaCmd");
                    setData(new HashMap<>() {{
                        put("clazz", CryptoUtil.encrypt(anButton.getJavaCmdBeanClass().getName()));
                        put("method", CryptoUtil.encrypt(anButton.getJavaCmdMethodName()));
                        // 30分钟过期
                        put("expireTime", CryptoUtil.encrypt(String.valueOf(System.currentTimeMillis() + 30 * 60 * 1000)));
                        put("&", "$$");
                    }});
                    setMessages(
                        new ApiMessage() {{
                            setSuccess("操作成功");
                            setFailed("操作失败");
                        }}
                    );

                }}
            );
        }
        return action;
    }


    public void processEditTabs(AnEntity anEntity, Tabs tabs) {
        List<AnColumnButton> anColumnButtons = anEntity.getColumnButtons();
        for (AnColumnButton anColumnButton : anColumnButtons) {
            if (!AmisCommonUtil.isPermissionGranted(AnnoBeanUtils.getBean(PermissionProxy.class), anColumnButton, anEntity)) {
                continue;
            }
            // 跳过TPL类型
            if (anColumnButton.isTplEnable()) {
                continue;
            }
            Tabs.Tab tab = AmisCommonUtil.createTabForColumnButton(anColumnButton);
            tabs.getTabs().add(tab);
        }
    }
}
