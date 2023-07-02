package site.sorghum.anno.modular.amis.model;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.PrimaryKey;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.*;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.input.InputTree;
import site.sorghum.amis.entity.layout.Page;
import site.sorghum.anno.exception.BizException;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoLeftTree;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.modular.anno.annotation.field.AnnoButton;
import site.sorghum.anno.modular.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import site.sorghum.anno.util.CryptoUtil;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * crud视图
 *
 * @author sorghum
 * @date 2023/07/02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CrudView extends Page {
    private CrudView() {
    }
    
    @Transient
    private Crud getCrudBody(){
        return (Crud) this.getBody().get(0);
    }
    /**
     * 添加过滤器
     *
     * @param clazz clazz
     */
    public void addCrudFilter(Class<?> clazz) {
        // 获取过滤的模板
        Form form = new Form();
        form.setTitle("条件搜索");
        form.setActions(CollUtil.newArrayList(
                new Action() {{
                    setType("submit");
                    setLevel("primary");
                    setLabel("搜索");
                }},
                new Action() {{
                    setType("reset");
                    setLabel("重置");
                }}
        ));
        List<AmisBase> body = new ArrayList<>();
        List<Field> fields = AnnoUtil.getAnnoFields(clazz);
        List<FormItem> amisColumns = new ArrayList<>();
        for (Field field : fields) {
            AnnoField annoField = field.getAnnotation(AnnoField.class);
            AnnoSearch search = annoField.search();
            if (search.enable()) {
                FormItem formItem = new FormItem();
                formItem.setName(field.getName());
                formItem.setLabel(annoField.title());
                formItem.setPlaceholder(search.placeHolder());
                formItem.setSize("sm");
                formItem = AnnoDataType.editorExtraInfo(formItem, annoField);
                amisColumns.add(formItem);
            }
        }
        if (amisColumns.size() == 0) {
            return;
        }
        // amisColumns 以4个为一组进行分组
        CollUtil.split(amisColumns, 4).forEach(columns -> {
            Group group = new Group() {{
                setBody(columns);
            }};
            body.add(group);
        });
        form.setBody(body);
        // 写入到当前对象
        Crud crudBody = getCrudBody();
        crudBody.setFilter(form);
    }

    /**
     * 添加列
     *
     * @param clazz clazz
     */
    public void addCrudColumns(Class<?> clazz) {
        List<Field> fields = AnnoUtil.getAnnoFields(clazz);
        List<Map> amisColumns = new ArrayList<>();
        for (Field field : fields) {
            AnnoField annoField = field.getAnnotation(AnnoField.class);
            Table.Column column = new Table.Column();
            column.setName(field.getName());
            column.setLabel(annoField.title());
            column.setSortable(true);
            Map<String, Object> amisColumn = AnnoDataType.displayExtraInfo(column, annoField);
            if (!annoField.show()) {
                amisColumn.put("toggled", false);
            }
            amisColumns.add(amisColumn);
        }
        Crud crudBody = getCrudBody();
        // 读取现有的列
        List<Map> columns = crudBody.getColumns();
        columns.addAll(0, amisColumns);
        // 重新写入
        crudBody.setColumns(columns);
    }

    public void addCrudDeleteButton(Class<?> clazz) {
        // 删除按钮模板
        Action delete = new Action();
        delete.setActionType("ajax");
        delete.setLabel("删除");
        delete.setLevel("danger");
        delete.setConfirmText("您确认要删除?");
        delete.setApi(new Api() {{
            setMethod("post");
            setUrl("/system/anno/${clazz}/removeById");
        }});
        // 读取现有的列
        Crud crudBody = getCrudBody();
        List<Map> columns = crudBody.getColumns();
        for (Map columnMap : columns) {
            if ("操作".equals(MapUtil.getStr(columnMap, "label"))) {
                // 添加删除按钮
                Object buttons = columnMap.get("buttons");
                if (buttons instanceof List<?> buttonList) {
                    List<Object> buttonListMap = (List<Object>) buttonList;
                    buttonListMap.add(delete);
                }
                break;
            }
        }
    }

    /**
     * 添加编辑信息
     *
     * @param clazz clazz
     */
    public void addCrudEditInfo(Class<?> clazz) {
        // 判断是否可以编辑
        List<Field> annoFields = AnnoUtil.getAnnoFields(clazz);
        boolean canEdit = annoFields.stream().map(f -> f.getAnnotation(AnnoField.class)).anyMatch(annoField -> annoField.edit().editEnable());
        if (!canEdit) {
            return;
        }
        Crud crudBody = getCrudBody();
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
    }

    public void addCrudColumnButtonInfo(Class<?> clazz) {
        List<Field> buttonFields = AnnoUtil.getAnnoButtonFields(clazz);
        // 读取现有的列
        Crud crudBody = getCrudBody();
        List<Map> columns = crudBody.getColumns();
        for (Map columnJson : columns) {
            if ("操作".equals(MapUtil.getStr(columnJson, "label"))) {
                for (Field buttonField : buttonFields) {
                    AnnoButton annoButton = AnnotationUtil.getAnnotation(buttonField, AnnoButton.class);
                    Action action = new Action();
                    AnnoButton.O2MJoinButton o2MJoinButton = annoButton.o2mJoinButton();
                    AnnoButton.M2MJoinButton m2mJoinButton = annoButton.m2mJoinButton();
                    if (o2MJoinButton.enable()) {
                        action = new DrawerButton();
                        action.setLabel(annoButton.name());
                        ((DrawerButton) action).setDrawer(
                                new DrawerButton.Drawer() {{
                                    setCloseOnEsc(true);
                                    setCloseOnOutside(true);
                                    setTitle(annoButton.name());
                                    setSize("xl");
                                    setBody(
                                            new IFrame() {{
                                                setType("iframe");
                                                setSrc("/system/config/amis/" + o2MJoinButton.joinAnnoMainClazz().getSimpleName() + "?" + o2MJoinButton.joinOtherClazzField() + "=${" + o2MJoinButton.joinThisClazzField() + "}");
                                            }}
                                    );
                                }}
                        );
                    } else if (m2mJoinButton.enable()) {
                        action = new DrawerButton();
                        HashMap<String, Object> queryMap = new HashMap<String, Object>() {{
                            put("joinValue", "${" + m2mJoinButton.joinThisClazzField() + "}");
                            put("joinCmd", Base64.encodeStr(m2mJoinButton.joinSql().getBytes(), false, true));
                            put("mediumThisField", m2mJoinButton.mediumThisField());
                            put("mediumOtherField", m2mJoinButton.mediumOtherField());
                            put("mediumTableClass", m2mJoinButton.mediumTableClass().getSimpleName());
                            put("joinThisClazzField", m2mJoinButton.joinThisClazzField());
                        }};
                        action.setLabel(annoButton.name());
                        ((DrawerButton) action).setDrawer(
                                new DrawerButton.Drawer() {{
                                    setCloseOnEsc(true);
                                    setCloseOnOutside(true);
                                    setSize("xl");
                                    setTitle(annoButton.name());
                                    setShowCloseButton(false);
                                    setBody(
                                            new IFrame() {{
                                                setType("iframe");
                                                setSrc("/system/config/amis-m2m/" + m2mJoinButton.joinAnnoMainClazz().getSimpleName() + "?" + URLUtil.buildQuery(queryMap, null));
                                            }}
                                    );
                                    setActions(
                                            new ArrayList<Action>() {{
                                                add(new Action() {{
                                                    setType("action");
                                                    setActionType("confirm");
                                                    setLabel("关闭");
                                                    setSize("lg");
                                                }});
                                            }}
                                    );
                                }}
                        );
                    } else if (StrUtil.isNotBlank(annoButton.jumpUrl())) {
                        action.setLabel(annoButton.name());
                        action.setActionType("url");
                        action.setUrl(annoButton.jumpUrl());
                    } else if (StrUtil.isNotBlank(annoButton.jsCmd())) {
                        action.setLabel(annoButton.name());
                        action.setOnClick(annoButton.jsCmd());
                    } else if (annoButton.javaCmd().enable()) {
                        action.setLabel(annoButton.name());
                        action.setActionType("ajax");
                        action.setApi(
                                new Api() {{
                                    setMethod("post");
                                    setUrl("/system/anno/runJavaCmd");
                                    setData(new HashMap<String, Object>() {{
                                        put("clazz", CryptoUtil.encrypt(annoButton.javaCmd().beanClass().getName()));
                                        put("method", CryptoUtil.encrypt(annoButton.javaCmd().methodName()));
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
                    } else {
                        continue;
                    }
                    // 添加对应按钮
                    Object buttons = columnJson.get("buttons");
                    if (buttons instanceof List<?> buttonList) {
                        List<Object> buttonListMap = (List<Object>) buttonList;
                        buttonListMap.add(action);
                        // 设置列宽
                        columnJson.put("width", buttonListMap.size() * 80);
                    }
                }
            }
        }
    }


    public void addCrudAddInfo(Class<?> clazz) {
        // 判断是否可以编辑
        List<Field> annoFields = AnnoUtil.getAnnoFields(clazz);
        boolean canAdd = annoFields.stream().map(f -> f.getAnnotation(AnnoField.class)).anyMatch(annoField -> annoField.edit().addEnable());
        if (!canAdd) {
            return;
        }
        List<AmisBase> formItems = new ArrayList<AmisBase>() {{
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
                if (edit.addEnable()) {
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
        Crud crudBody = getCrudBody();
        Form filter = crudBody.getFilter();
        List<Action> actions = filter.getActions();
        DialogButton dialogButton = new DialogButton();
        dialogButton.setLabel("新增");
        dialogButton.setIcon("fa fa-plus pull-left");
        dialogButton.setLevel("primary");
        dialogButton.setDialog(
                new DialogButton.Dialog() {{
                    setTitle("新增");
                    setBody(
                            new Form() {{
                                setWrapWithPanel(false);
                                setReload("crud_template_main");
                                setApi(new Api() {{
                                    setMethod("post");
                                    setUrl("/system/anno/${clazz}/save");
                                }});
                                setId("simple-edit-form");
                                setBody(formItems);
                            }}
                    );
                }}
        );
        actions.add(0, dialogButton);
    }

    /**
     * 添加树边栏
     *
     * @param clazz      clazz
     * @param properties properties
     */
    public void addCommonTreeAside(Class<?> clazz, Map<String, Object> properties) {
        AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
        AnnoLeftTree annoLeftTree = annoMain.annoLeftTree();
        InputTree tree = new InputTree();
        tree.setId("aside-input-tree");
        tree.setName("_cat");
        tree.setSearchable(true);
        tree.setMultiple(false);
        tree.setCascade(false);
        tree.setHeightAuto(false);
        tree.setVirtualThreshold(9999);
        tree.setInputClassName("no-border no-padder mt-1");
        tree.setUnfoldedLevel(2);
        tree.setShowOutline(true);
        tree.setSource(new Api() {{
            setMethod("get");
            setUrl("/system/anno/${treeClazz}/annoTrees");
        }});
        Map<String, Object> event = new HashMap<>();
        event.put("change", new HashMap<String, Object>() {{
            put("actions", CollUtil.newArrayList(
                    new HashMap<String, Object>() {{
                        put("actionType", "broadcast");
                        put("args", new HashMap<String, Object>() {{
                            put("eventName", "broadcast_aside_change");
                        }});
                        put("data", new HashMap<String, Object>() {{
                            put("_cat", "${_cat}");
                        }});
                    }}
            ));
        }});
        tree.setOnEvent(event);
        if (annoLeftTree.enable()) {
            setAside(tree);
            return;
        }
        AnnoTree annoTree = annoMain.annoTree();
        if (annoTree.enable() && annoTree.displayAsTree()) {
            setAside(tree);
            return;
        }
    }

    /**
     * 添加crud m2m复选框
     *
     * @param clazz clazz
     */
    public void addCrudM2mCheckBox(Class<?> clazz) {
        Crud crudBody = getCrudBody();
        Api api = crudBody.getApi();
        Map<String, Object> data = api.getData();
        if (data == null) {
            data = new HashMap<>();
            api.setData(data);
        }
        data.put("reverseM2m", true);
        List<Action> bulkActions = crudBody.getBulkActions();
        if (bulkActions == null) {
            bulkActions = new ArrayList<>();
            crudBody.setBulkActions(bulkActions);
        }
        Action insertRelations = new Action();
        insertRelations.setLabel("批量新增关系");
        insertRelations.setActionType("ajax");
        insertRelations.setLevel("primary");
        insertRelations.setApi(new Api(){{
            setMethod("post");
            setUrl("/system/anno/${clazz}/addM2m");
            setData(new HashMap<String, Object>(){{
                put("&", "$$");
                put("_extraData", "${extraData}");
            }});
            setMessages(new ApiMessage(){{
                setSuccess("操作成功");
                setFailed("操作失败");
            }});
        }});
        bulkActions.add(insertRelations);
    }

    //----------------------静态方法初始化---------------------- 
    
    public static CrudView of(){
        return crudBasePage();
    }

    private static CrudView crudBasePage(){
        CrudView page = new CrudView();
        page.setAsideResizor(true);
        Crud bodyCrud = new Crud();
        bodyCrud.setId("crud_template_main");
        bodyCrud.setDraggable(false);
        bodyCrud.setPerPage(10);
        bodyCrud.setSyncLocation(false);
        bodyCrud.setApi(
                new Api(){{
                    setMethod("post");
                    setUrl("/system/anno/${clazz}/page");
                    setData(new HashMap<>(){{
                        put("&", "$$");
                        put("_cat", "${_cat}");
                        put("ignoreM2m", false);
                        put("reverseM2m", false);
                        put("_extraData", "${extraData}");
                    }});
                }}
        );
        bodyCrud.setHeaderToolbar(CollUtil.newArrayList("export-excel", "bulkActions", "reload"));
        bodyCrud.setFooterToolbar(CollUtil.newArrayList("statistics", "switch-per-page", "pagination"));
        bodyCrud.setColumns(
                new ArrayList<>(){{
                    add(
                            new HashMap<String,Object>(){{
                                put("type","operation");
                                put("label","操作");
                                put("buttons",new ArrayList<>());
                                put("fixed","right");
                            }}
                    );
                }}
        );
        page.setBody(CollUtil.newArrayList(bodyCrud));
        page.setAsideMinWidth(220);
        page.setAsideMaxWidth(350);
        return page;
    }

}
