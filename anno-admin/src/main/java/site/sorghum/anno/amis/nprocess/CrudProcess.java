package site.sorghum.anno.amis.nprocess;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.amis.AmisTemplate;
import site.sorghum.amis.AmisTemplateModel;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.DialogButton;
import site.sorghum.amis.entity.display.Group;
import site.sorghum.amis.entity.display.IFrame;
import site.sorghum.amis.entity.display.Table;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.layout.Tabs;
import site.sorghum.anno._common.util.CryptoUtil;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._metadata.*;
import site.sorghum.anno.amis.util.AmisCommonUtil;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.proxy.PermissionProxy;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.hutool.core.collection.CollUtil.split;

@Named
public class CrudProcess {
    @Inject
    MetadataManager metadataManager;

    @Inject
    PermissionProxy permissionProxy;

    public Map<String, Object> process(Class<?> clazz, Map<String, Object> properties) {
        AnEntity anEntity = metadataManager.getEntity(clazz);
        List<AnField> fields = anEntity.getFields();
        List<AnColumnButton> anColumnButtons = anEntity.getColumnButtons();
        List<AnTableButton> tableButtons = anEntity.getTableButtons();
        ArrayList<AmisTemplateModel> templateModels = new ArrayList<>();
        ArrayList<String> regions = CollUtil.newArrayList(
            "body"
        );

        // -------------- 左侧树 --------------
        boolean asideEnable = anEntity.isEnableLeftTree();
        if (asideEnable) {
            regions.add("aside");
        }
        // -------------- 新增按钮 --------------
        List<AmisBase> addFormItems = new ArrayList<>();
        boolean canAdd = fields.stream().anyMatch(AnField::isAddEnable);
        if (canAdd) {
            addFormItems = AmisCommonUtil.formItemToGroup(anEntity, generateFormItems(fields), 2);
        }

        //----------------- 查询信息 --------------
        List<AmisBase> filterFormItems = createFilterFormItems(anEntity);
        // 设置默认排序数据
        Map<String, Object> data = new HashMap<>();
        data.put("orderBy", anEntity.getOrderValue());
        data.put("orderDir", anEntity.getOrderType());

        // ---------------- 表按钮 ----------------
        List<Object> tableButtonListMap = new ArrayList<>();
        for (AnTableButton anButton : tableButtons) {
            boolean permissionGranted = AmisCommonUtil.isPermissionGranted(permissionProxy, anButton, anEntity);
            if (!permissionGranted) {
                continue;
            }
            Action action = createTableAction(anButton);
            tableButtonListMap.add(action);
        }
        String tableButtonListMapStr = JSONUtil.toJsonString(tableButtonListMap);
        // 去除前后的[]
        tableButtonListMapStr = tableButtonListMapStr.substring(1, tableButtonListMapStr.length() - 1);
        // -------------- 列信息 --------------
        List<Map> amisColumns = fields.stream()
            .map(field -> createAmisColumn(field))
            .collect(Collectors.toList());
        String amisColumnsStr = JSONUtil.toJsonString(amisColumns);
        // 去除前后的[]
        amisColumnsStr = amisColumnsStr.substring(1, amisColumnsStr.length() - 1);


        // -------------- 列按钮 --------------
        List<Object> buttonListMap = new ArrayList<>();
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

        // -------------- 详情 删除按钮 --------------

        // 判断是否可以删除
        if (anEntity.isCanRemove()) {
            Action delete = createDeleteAction(clazz);
            buttonListMap.add(0, delete);
        }

        // 判断是否可以编辑
        boolean canEdit = fields.stream().anyMatch(AnField::isEditEnable);
        if (canEdit) {
            DialogButton dialogButton = createEditDialogButton(fields, anEntity);
            buttonListMap.add(0, dialogButton);
        }

        templateModels.add(new AmisTemplateModel("regions", regions));
        templateModels.add(new AmisTemplateModel("tableFilterButtons", tableButtonListMapStr));
        templateModels.add(new AmisTemplateModel("globalFilterFormSearchBody", filterFormItems));
        templateModels.add(new AmisTemplateModel("filterData", data));
        templateModels.add(new AmisTemplateModel("globalAddFormBody", addFormItems));
        templateModels.add(new AmisTemplateModel("columns", amisColumnsStr));
        templateModels.add(new AmisTemplateModel("columnOperatorButtons", buttonListMap));
        templateModels.add(new AmisTemplateModel("columnOperatorButtonsWidth", buttonListMap.size() * 120));


        String content = ResourceUtil.readStr("classpath:crudTemplate.json", Charset.defaultCharset());
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
        String src = "/index.html#/amisSingle/index/" + anColumnButton.getO2mJoinMainClazz().getSimpleName()
                     + "?" + anColumnButton.getO2mJoinOtherField() + "=${" + anColumnButton.getO2mJoinThisField() + "}";
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
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("joinValue", "${" + anColumnButton.getM2mJoinThisClazzField() + "}");
        queryMap.put("joinCmd", Base64.encodeStr(anColumnButton.getM2mJoinSql().getBytes(), false, true));
        queryMap.put("mediumThisField", anColumnButton.getM2mMediumOtherField());
        queryMap.put("mediumOtherField", anColumnButton.getM2mMediumThisField());
        queryMap.put("mediumTableClass", anColumnButton.getM2mMediumTableClass().getSimpleName());
        queryMap.put("joinThisClazzField", anColumnButton.getM2mJoinThisClazzField());
        queryMap.put("isM2m", true);

        DialogButton.Dialog dialog = new DialogButton.Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setTitle(anColumnButton.getName());
        dialog.setSize(anColumnButton.getM2mWindowSize());
        dialog.setShowCloseButton(true);
        IFrame iFrame = new IFrame();
        iFrame.setSrc("/index.html#/amisSingle/index/" + anColumnButton.getM2mJoinAnnoMainClazz().getSimpleName() + "?" + URLUtil.buildQuery(queryMap, null));
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

    private Action createDeleteAction(Class<?> clazz) {
        Action delete = new Action();
        delete.setActionType("ajax");
        delete.setLabel("删除");
        delete.setLevel("danger");
        delete.setConfirmText("您确认要删除?");
        delete.setApi(createDeleteApi(clazz));
        return delete;
    }

    private Api createDeleteApi(Class<?> clazz) {
        Api api = new Api();
        api.setMethod("post");
        api.setUrl("/amis/system/anno/" + clazz.getSimpleName() + "/removeById");
        return api;
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
            if (!AmisCommonUtil.isPermissionGranted(permissionProxy, anColumnButton, anEntity)) {
                continue;
            }
            Tabs.Tab tab = AmisCommonUtil.createTabForColumnButton(anColumnButton);
            tabs.getTabs().add(tab);
        }
    }

    private Map<String, Object> createFilterEvent() {
        return new HashMap<>() {{
            put("broadcast_aside_change", new HashMap<>() {{
                put("actions", CollUtil.newArrayList(new HashMap<>() {{
                    put("actionType", "reload");
                    put("componentId", "crud_template_main");
                }}));
            }});
        }};
    }

    private List<AmisBase> createFilterFormItems(AnEntity anEntity) {
        List<AnField> fields = anEntity.getFields();
        List<AmisBase> formItems = fields.stream()
            .filter(AnField::isSearchEnable)
            .map(field -> createFilterFormItem(field))
            .collect(Collectors.toList());

        List<AmisBase> body = new ArrayList<>();
        split(formItems, 4).forEach(columns -> {
            Group group = new Group() {{
                setBody(columns);
            }};
            body.add(group);
        });

        return body;
    }

    private FormItem createFilterFormItem(AnField field) {
        FormItem formItem = new FormItem();
        formItem.setName(field.getFieldName());
        formItem.setLabel(field.getTitle());
        formItem.setPlaceholder(field.getSearchPlaceHolder());
        formItem.setSize(field.getSearchSize());
        formItem.setColumnRatio("3");
        return AnnoDataType.editorExtraInfo(formItem, field);
    }

    public Action createTableAction(AnTableButton anButton) {
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
}
