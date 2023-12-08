package site.sorghum.anno.amis.nprocess;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.amis.AmisTemplate;
import site.sorghum.amis.AmisTemplateModel;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.DialogButton;
import site.sorghum.amis.entity.display.IFrame;
import site.sorghum.amis.entity.display.Table;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.layout.Tabs;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.util.CryptoUtil;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._metadata.*;
import site.sorghum.anno.amis.util.AmisCommonUtil;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.proxy.PermissionProxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
public class CrudProcess extends BaseProcess {
    @Inject
    MetadataManager metadataManager;

    @Inject
    PermissionProxy permissionProxy;


    public Object process(Class<?> clazz, Map<String, Object> properties) {
        boolean isM2m = properties.containsKey("isM2m") && Boolean.TRUE.equals(properties.get("isM2m"));
        String content = getTemplateContent("crud_template");

        AnEntity anEntity = metadataManager.getEntity(clazz);
        List<AnField> fields = anEntity.getFields();
        List<AnColumnButton> anColumnButtons = anEntity.getColumnButtons();
        List<AnButton> tableButtons = anEntity.getTableButtons();
        ArrayList<AmisTemplateModel> templateModels = new ArrayList<>();
        ArrayList<String> regions = CollUtil.newArrayList(
            "body"
        );

        // -------------- 左侧树 --------------
        boolean asideEnable = anEntity.isEnableLeftTree();
        if (!isM2m && asideEnable) {
            regions.add("aside");
        }
        // -------------- 新增按钮 --------------
        List<AmisBase> addFormItems = new ArrayList<>();
        boolean canAdd = fields.stream().anyMatch(AnField::isAddEnable);
        if (!isM2m &&canAdd) {
            addFormItems = AmisCommonUtil.formItemToGroup(anEntity, generateAddFormItems(fields), 2);
        }

        //----------------- 查询信息 --------------
        List<AmisBase> filterFormItems = createFilterFormItems(anEntity);

        //----------------- 设置初始化数据 --------------
        Map<String, Object> data = new HashMap<>();
//        data.put("orderBy", anEntity.getOrderValue());
//        data.put("orderDir", anEntity.getOrderType());

        // 多对多新增按钮
        List<Action> bulkActions = new ArrayList<>();
        if (isM2m) {
            Action insertRelations = new Action();
            insertRelations.setLabel("批量新增关系");
            insertRelations.setActionType("ajax");
            insertRelations.setLevel("primary");
            insertRelations.setApi(new Api() {{
                setMethod("post");
                setUrl("/amis/system/anno/${clazz}/addM2m");
                setData(new HashMap<>() {{
                    put("&", "$$");
                    put("_extraData", "${extraData}");
                }});
                setMessages(new ApiMessage() {{
                    setSuccess("操作成功");
                    setFailed("操作失败");
                }});
            }});
            insertRelations.setReload("m2m-crud,crud_template_main");
            bulkActions.add(insertRelations);
        }
        // ---------------- 表按钮 ----------------
        String tableButtonListMapStr = "";
        if (!isM2m) {
            List<Object> tableButtonListMap = new ArrayList<>();
            for (AnButton anButton : tableButtons) {
                boolean permissionGranted = AmisCommonUtil.isPermissionGranted(permissionProxy, anButton, anEntity);
                if (!permissionGranted) {
                    continue;
                }
                Action action = createTableAction(anButton);
                tableButtonListMap.add(action);
            }
            tableButtonListMapStr = JSONUtil.toJsonString(tableButtonListMap);
            // 去除前后的[]
            tableButtonListMapStr = tableButtonListMapStr.substring(1, tableButtonListMapStr.length() - 1);
        }
        // -------------- 列信息 --------------
        List<Map> amisColumns = fields.stream()
            .map(field -> createAmisColumn(field))
            .collect(Collectors.toList());
        String amisColumnsStr = JSONUtil.toJsonString(amisColumns);
        // 去除前后的[]
        amisColumnsStr = amisColumnsStr.substring(1, amisColumnsStr.length() - 1);


        // -------------- 列按钮 --------------
        List<Object> buttonListMap = new ArrayList<>();

        if (!isM2m) {
            for (AnColumnButton anColumnButton : anColumnButtons) {
                boolean permissionGranted = AmisCommonUtil.isPermissionGranted(permissionProxy, anColumnButton, anEntity);
                if (!permissionGranted) {
                    continue;
                }
                Action action = createActionForButton(anColumnButton);
                if (action != null) {
                    buttonListMap.add(action);
                }
            }
        }

        // -------------- 详情 删除按钮 --------------

        // 判断是否可以删除
        if (!isM2m && anEntity.isCanRemove()) {
            Action delete = createDeleteAction(clazz);
            buttonListMap.add(0, delete);
        }

        // 判断是否可以编辑
        boolean canEdit = fields.stream().anyMatch(AnField::isEditEnable);
        if (!isM2m &&canEdit) {
            DialogButton dialogButton = createEditDialogButton(fields, anEntity);
            buttonListMap.add(0, dialogButton);
        }

        // 新增详情按钮
        if (!isM2m) {
            buttonListMap.add(0, createDetailDialogButton(fields, anEntity));
        }

        templateModels.add(new AmisTemplateModel("reverseM2m", isM2m));
        templateModels.add(new AmisTemplateModel("bulkActions",bulkActions));
        templateModels.add(new AmisTemplateModel("regions", regions));
        templateModels.add(new AmisTemplateModel("tableFilterButtons", tableButtonListMapStr));
        templateModels.add(new AmisTemplateModel("globalFilterFormSearchBody", filterFormItems));
        templateModels.add(new AmisTemplateModel("initData", data));

        templateModels.add(new AmisTemplateModel("globalAddFormBody", addFormItems));
        templateModels.add(new AmisTemplateModel("hiddenGlobalAddFormBody",addFormItems.isEmpty()));

        templateModels.add(new AmisTemplateModel("columns", amisColumnsStr));

        templateModels.add(new AmisTemplateModel("columnOperatorButtons", buttonListMap));
        templateModels.add(new AmisTemplateModel("hiddenColumnOperatorButtons",buttonListMap.isEmpty()));
        templateModels.add(new AmisTemplateModel("columnOperatorButtonsWidth", buttonListMap.size() * 120));


        AmisTemplate amisTemplate = new AmisTemplate(content, templateModels);
        return JSONUtil.toBean(amisTemplate.getFilledContent(), HashMap.class);
    }

    private Map<String, Object> createAmisColumn(AnField field) {
        Table.Column column = new Table.Column();
        column.setName(field.getFieldName());
        column.setLabel(field.getTitle());
        column.setSortable(true);

        Map<String, Object> amisColumn = AnnoDataType.displayExtraInfo(column, field);

        if (!field.isShow()) {
            amisColumn.put("toggled", false);
        }

        return amisColumn;
    }


    private Action createActionForButton(AnColumnButton anColumnButton) {
        Action action = null;
        if (anColumnButton.isO2mEnable()) {
            action = createO2MDialogButton(anColumnButton);
        } else if (anColumnButton.isM2mEnable()) {
            action = createM2mDialogButton(anColumnButton);
        } else if (StrUtil.isNotBlank(anColumnButton.getJumpUrl())) {
            action = createActionUrl(anColumnButton.getName(), anColumnButton.getJumpUrl());
        } else if (StrUtil.isNotBlank(anColumnButton.getJsCmd())) {
            action = createActionJsCmd(anColumnButton.getName(), anColumnButton.getJsCmd());
        } else if (anColumnButton.isJavaCmdEnable()) {
            action = createJavaCmdAction(anColumnButton);
        } else if (anColumnButton.isTplEnable()) {
            action = createTplDialogButton(anColumnButton);
        }
        return action;
    }

    private DialogButton createO2MDialogButton(AnColumnButton anColumnButton) {
        String label = anColumnButton.getName();
        String windowSize = anColumnButton.getO2mWindowSize();
        String src = AnnoConstants.BASE_URL + "/index.html#/amisSingle/index/" + anColumnButton.getO2mTargetClass().getSimpleName()
                     + "?" + anColumnButton.getO2mTargetJavaField() + "=${" + anColumnButton.getO2mThisJavaField() + "}";
        DialogButton dialogButton = new DialogButton();
        dialogButton.setLabel(label);
        DialogButton.Dialog dialog = new DialogButton.Dialog();
        dialog.setTitle(label);
        dialog.setShowCloseButton(true);
        dialog.setSize(windowSize);
        dialog.setActions(new ArrayList<>());
        IFrame iFrame = new IFrame();
        iFrame.setHeight(anColumnButton.getO2mWindowHeight());
        iFrame.setSrc(src);
        dialog.setBody(iFrame);
        dialogButton.setDialog(dialog);
        return dialogButton;
    }

    private Action createM2mDialogButton(AnColumnButton anColumnButton) {
        DialogButton dialogButton = new DialogButton();
        dialogButton.setLabel(anColumnButton.getName());
        Map<String, Object> queryMap = AmisCommonUtil.createM2mJoinQueryMap(anColumnButton, true);
        DialogButton.Dialog dialog = new DialogButton.Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setTitle(anColumnButton.getName());
        dialog.setSize(anColumnButton.getM2mWindowSize());
        dialog.setShowCloseButton(true);
        IFrame iFrame = new IFrame();
        iFrame.setSrc(AnnoConstants.BASE_URL + "/index.html#/amisSingle/index/" + anColumnButton.getM2mJoinTargetClazz().getSimpleName() + "?" + URLUtil.buildQuery(queryMap, null));
        iFrame.setHeight(anColumnButton.getM2mWindowHeight());
        dialog.setBody(iFrame);
        dialogButton.setDialog(dialog);
        dialog.setActions(new ArrayList<>());

        dialogButton.setDialog(dialog);
        return dialogButton;
    }

    private Action createActionUrl(String label, String url) {
        Action action = new Action();
        action.setLabel(label);
        action.setActionType("url");
        action.setUrl(url);
        return action;
    }

    private Action createActionJsCmd(String label, String jsCmd) {
        Action action = new Action();
        action.setLabel(label);
        action.setOnClick(jsCmd);
        return action;
    }

    private Action createJavaCmdAction(AnColumnButton anColumnButton) {
        Action action = new Action();
        action.setLabel(anColumnButton.getName());
        action.setActionType("ajax");
        Api api = new Api();
        api.setMethod("post");
        api.setUrl("/amis/system/anno/runJavaCmd");
        Map<String, Object> data = new HashMap<>();
        data.put("clazz", CryptoUtil.encrypt(anColumnButton.getJavaCmdBeanClass().getName()));
        data.put("method", CryptoUtil.encrypt(anColumnButton.getJavaCmdMethodName()));
        // 30分钟过期
        data.put("expireTime", CryptoUtil.encrypt(String.valueOf(System.currentTimeMillis() + 30 * 60 * 1000)));
        data.put("&", "$$");
        api.setData(data);
        Api.ApiMessage messages = new Api.ApiMessage();
        messages.setSuccess("操作成功");
        messages.setFailed("操作失败");
        api.setMessages(messages);
        action.setApi(api);
        return action;
    }

    private Action createTplDialogButton(AnColumnButton anColumnButton) {
        DialogButton dialogButton = new DialogButton();
        dialogButton.setLabel(anColumnButton.getName());
        DialogButton.Dialog dialog = new DialogButton.Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setTitle(anColumnButton.getName());
        dialog.setShowCloseButton(true);
        dialog.setSize(anColumnButton.getTplWindowSize());
        dialog.setActions(new ArrayList<>());
        IFrame iFrame = new IFrame();
        iFrame.setType("iframe");
        iFrame.setSrc("/annoTpl/" + anColumnButton.getTplClazz().getSimpleName() + "/" + anColumnButton.getTplName());
        iFrame.setHeight(anColumnButton.getTplWindowHeight());
        dialog.setBody(iFrame);
        dialogButton.setDialog(dialog);
        return dialogButton;
    }


    private List<AmisBase> generateAddFormItems(List<AnField> anFields) {
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

    private DialogButton createEditDialogButton(List<AnField> fields, AnEntity entity) {
        DialogButton dialogButton = new DialogButton();
        dialogButton.setLabel("编辑");

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
        dialog.setTitle("编辑");
        dialog.setBody(form);

        dialogButton.setDialog(dialog);

        return dialogButton;
    }
}
