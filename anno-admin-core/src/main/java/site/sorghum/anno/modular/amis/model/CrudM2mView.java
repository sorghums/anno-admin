package site.sorghum.anno.modular.amis.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.PrimaryKey;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.display.DialogButton;
import site.sorghum.amis.entity.display.Group;
import site.sorghum.amis.entity.display.Table;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.layout.Page;
import site.sorghum.anno.exception.BizException;
import site.sorghum.anno.modular.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.anno.util.AnnoUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Crud多对多视图
 *
 * @author sorghum
 * @date 2023/07/02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CrudM2mView extends Page {

    private CrudM2mView() {

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

    /**
     * 添加crud关系数据
     *
     * @param clazz    clazz
     * @param amisBase amisBase
     */
    public void addRelationCrudData(Class<?> clazz, AmisBase amisBase) {
        Crud crudBody = getCrudBody();
        List<Object> headerToolbar = crudBody.getHeaderToolbar();
        Object obj = headerToolbar.get(2);
        DialogButton dialogButton = (DialogButton) obj;
        DialogButton.Dialog dialog = dialogButton.getDialog();
        dialog.setBody(amisBase);
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


    public void addDeleteRelationEditInfo(Class<?> clazz) {
        // 删除按钮模板
        Action delete = new Action();
        delete.setType("button");
        delete.setActionType("ajax");
        delete.setLevel("danger");
        delete.setLabel("删除关联关系");
        delete.setConfirmText("您确认要删除关联关系吗?");
        delete.setApi(
                new Api() {{
                    setMethod("post");
                    setUrl("/system/anno/${clazz}/remove-relation");
                    setData(new JSONObject() {{
                        put("&", "$$");
                        put("_extraData", "${extraData}");
                    }});
                    setMessages(
                            new ApiMessage() {{
                                setSuccess("操作成功");
                                setFailed("操作失败");
                            }}
                    );
                }}
        );
        Crud crudBody = getCrudBody();
        // 读取现有的列
        List<Map> columns = crudBody.getColumns();
        for (Map columnJson : columns) {
            if ("操作".equals(MapUtil.getStr(columnJson, "label"))) {
                // 添加删除按钮
                Object buttons = columnJson.get("buttons");
                if (buttons instanceof List<?> buttonList) {
                    List<Object> buttonListCommon = (List<Object>) buttonList;
                    // 设置列宽
                    buttonListCommon.add(delete);
                    columnJson.put("width", buttonListCommon.size() * 80);
                }
            }
        }
    }


    private Crud getCrudBody() {
        return (Crud) getBody().get(0);
    }

    public static CrudM2mView of() {
        return crudM2mView();
    }

    private static CrudM2mView crudM2mView() {
        CrudM2mView crudM2mView = new CrudM2mView();
        crudM2mView.setAsideResizor(true);
        crudM2mView.setAsideMinWidth(220);
        crudM2mView.setAsideMaxWidth(350);
        Crud bodyCrud = new Crud();
        bodyCrud.setDraggable(false);
        bodyCrud.setPerPage(10);
        bodyCrud.setId("m2m-crud");
        bodyCrud.setSyncLocation(false);
        bodyCrud.setApi(
                new Api() {{
                    setMethod("post");
                    setUrl("/system/anno/${clazz}/page");
                    setData(
                            new HashMap<>() {{
                                put("_cat", "${_cat}");
                                put("_extraData", "${extraData}");
                                put("&", "$$");
                            }}
                    );
                }}
        );
        bodyCrud.setOnEvent(
                new HashMap<>() {{
                    put("broadcast_refresh_m2m_crud", new HashMap<>() {{
                        put("actions", CollUtil.newArrayList(
                                new HashMap<>() {{
                                    put("actionType", "reload");
                                    put("componentId", "m2m-crud");
                                }}
                        ));
                    }});
                }}
        );
        bodyCrud.setKeepItemSelectionOnPageChange(true);
        bodyCrud.setHeaderToolbar(
                CollUtil.newArrayList(
                        "bulkActions",
                        "reload",
                        new DialogButton() {{
                            setLabel("新增关联关系");
                            setLevel("primary");
                            setDialog(
                                    new DialogButton.Dialog() {{
                                        setTitle("新增关联关系");
                                        setSize("full");
                                        setActions(
                                                CollUtil.newArrayList(
                                                        new Action() {{
                                                            setLabel("关闭");
                                                            setActionType("confirm");
                                                            setSize("lg");
                                                            setLevel("info");
                                                        }}
                                                )
                                        );
                                        setShowCloseButton(false);
                                        setOnEvent(
                                                new HashMap<>() {{
                                                    put("confirm", new HashMap<>() {{
                                                        put("actions", CollUtil.newArrayList(
                                                                new HashMap<>() {{
                                                                    put("actionType", "broadcast");
                                                                    put("args", new HashMap<>() {{
                                                                        put("eventName", "broadcast_refresh_m2m_crud");
                                                                    }});
                                                                    put("data", new HashMap<>() {{
                                                                    }});
                                                                }}
                                                        ));
                                                    }});
                                                }}
                                        );
                                    }}
                            );
                        }}
                )
        );
        bodyCrud.setFooterToolbar(
                CollUtil.newArrayList(
                        "statistics",
                        "switch-per-page",
                        "pagination"

                )
        );
        bodyCrud.setColumns(
                CollUtil.newArrayList(
                        new HashMap<>() {{
                            put("type", "operation");
                            put("label", "操作");
                            put("buttons", new ArrayList<>());
                            put("fixed", 100);
                        }}
                )
        );
        crudM2mView.setBody(CollUtil.newArrayList(bodyCrud));
        return crudM2mView;
    }
}
