package site.sorghum.anno.modular.amis.model;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.noear.wood.annotation.PrimaryKey;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.Group;
import site.sorghum.amis.entity.display.Table;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.input.InputTree;
import site.sorghum.amis.entity.input.TreeSelect;
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
import site.sorghum.anno.modular.anno.util.TemplateUtil;
import site.sorghum.anno.util.CryptoUtil;
import site.sorghum.anno.util.JSONUtil;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Amis
 *
 * @author sorghum
 * @since 2023/05/20
 */
public class Amis extends HashMap<String ,Object> {


    //---------------------- 以下为CRUD的表代码 ----------------------//

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
        tree.setSource(new Api(){{
            setMethod("get");
            setUrl("/system/anno/${treeClazz}/annoTrees");
        }});
        Map<String,Object> event = new HashMap<>();
        event.put("change", new HashMap<String,Object>(){{
            put("actions", List.of(
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
            JSONUtil.write(this, "$.aside", tree);
            return;
        }
        AnnoTree annoTree = annoMain.annoTree();
        if (annoTree.enable() && annoTree.displayAsTree()) {
            JSONUtil.write(this, "$.aside", tree);
            return;
        }
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
        form.setActions(List.of(
                new Action(){{
                    setType("submit");
                    setLevel("primary");
                    setLabel("搜索");
                }},
                new Action(){{
                    setType("reset");
                    setLabel("重置");
                }}
        ));
        Map<String ,Object> filter = TemplateUtil.getTemplate("item/filter.json");
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
            Group group = new Group(){{
                setBody(columns);
            }};
            body.add(group);
        });
        form.setBody(body);
        // 写入到当前对象
        JSONUtil.write(this, "$.body.filter", form);
    }

    /**
     * 添加列
     *
     * @param clazz clazz
     */
    public void addCrudColumns(Class<?> clazz) {
        List<Field> fields = AnnoUtil.getAnnoFields(clazz);
        List<JSONObject> amisColumns = new ArrayList<>();
        for (Field field : fields) {
            AnnoField annoField = field.getAnnotation(AnnoField.class);
            JSONObject amisColumn = new JSONObject();
            amisColumn.put("name", field.getName());
            amisColumn.put("label", annoField.title());
            amisColumn.put("sortable", true);
            AnnoDataType.displayExtraInfo(amisColumn, annoField);
            if (!annoField.show()) {
                amisColumn.put("toggled", false);
            }
            amisColumns.add(amisColumn);
        }
        // 读取现有的列
        List<JSONObject> columns = JSONUtil.readList(this, "$.body.columns", JSONObject.class);
        columns.addAll(0, amisColumns);
        // 重新写入
        JSONUtil.write(this, "$.body.columns", columns);
    }


    /**
     * 添加crud m2m复选框
     *
     * @param clazz clazz
     */
    public void addCrudM2mCheckBox(Class<?> clazz) {
        JSONUtil.write(this, "$.body.api.data.reverseM2m",true);
        JSONUtil.write(this, "$.body.bulkActions",new ArrayList<Map<String,Object>>(){{
            add(new HashMap<String,Object>(){{
                put("label","批量新增关系");
                put("actionType","ajax");
                put("api",new HashMap<String,Object>(){{
                    put("url","/system/anno/${clazz}/addM2m");
                    put("method","post");
                    put("data",new HashMap<String,Object>(){{
                        put("&","$$");
                        put("_extraData","${extraData}");
                    }});
                    put("messages",new HashMap<String,Object>(){{
                        put("success","操作成功");
                        put("failed","操作失败");
                    }});
                }});
            }});
        }});
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
        List<JSONObject> columns = JSONUtil.readList(this, "$.body.columns", JSONObject.class);
        JSONObject columnJson = columns.stream().filter(column -> "操作".equals(column.getString("label"))).findFirst().orElseThrow(
                () -> new BizException("操作列不存在")
        );
        columnJson.getJSONArray("buttons").add(new JSONObject() {{
            // 编辑按钮
            put("type", "button");
            put("label", "编辑");
            put("actionType", "dialog");
            // 弹出框
            put("dialog", new JSONObject() {{
                // 弹出框标题
                put("title", "编辑");
                // 弹出框内容
                put("body", new JSONObject() {{
                    // 弹出框内容类型
                    put("type", "form");
                    put("name", "simple-edit-form");
                    // 提交API
                    put("api", "post:/system/anno/${clazz}/updateById");
                    // 表单控件
                    put("controls", new JSONArray() {{
                        List<Field> fields = AnnoUtil.getAnnoFields(clazz);
                        for (Field field : fields) {
                            AnnoField annoField = field.getAnnotation(AnnoField.class);
                            PrimaryKey annoId = field.getAnnotation(PrimaryKey.class);
                            if (annoId != null) {
                                add(new FormItem(){{
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
                    }});
                }});

            }});
        }});
        JSONUtil.write(this, "$.body.columns", columns);
    }

    public void addCrudAddInfo(Class<?> clazz) {
        // 判断是否可以编辑
        List<Field> annoFields = AnnoUtil.getAnnoFields(clazz);
        boolean canAdd = annoFields.stream().map(f -> f.getAnnotation(AnnoField.class)).anyMatch(annoField -> annoField.edit().addEnable());
        if (!canAdd) {
            return;
        }
        List<JSONObject> action = JSONUtil.readList(this, "$.body.filter.actions", JSONObject.class);
        action.add(0, new JSONObject() {{
            put("type", "button");
            put("actionType", "dialog");
            put("label", "新增");
            put("icon", "fa fa-plus pull-left");
            put("primary", true);
            put("dialog", new JSONObject() {{
                put("title", "新增");
                put("body", new JSONObject() {{
                    put("type", "form");
                    put("reload", "crud_template_main");
                    put("name", "simple-edit-form");
                    put("api", "post:/system/anno/${clazz}/save");
                    put("controls", new JSONArray() {{
                        List<Field> fields = AnnoUtil.getAnnoFields(clazz);
                        for (Field field : fields) {
                            AnnoField annoField = field.getAnnotation(AnnoField.class);
                            PrimaryKey annoId = field.getAnnotation(PrimaryKey.class);
                            if (annoId != null) {
                                add(new FormItem(){{
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
                    }});
                }});

            }});
        }});
        JSONUtil.write(this, "$.body.filter.actions", action);
    }

    public void addCrudDeleteButton(Class<?> clazz) {
        // 删除按钮模板
        JSONObject deleteJsonObj = new JSONObject() {{
            put("type", "button");
            put("actionType", "ajax");
            put("level", "danger");
            put("label", "删除");
            put("confirmText", "您确认要删除?");
            put("api", "post:/system/anno/${clazz}/removeById");
        }};
        // 读取现有的列
        List<JSONObject> columns = JSONUtil.readList(this, "$.body.columns", JSONObject.class);
        for (JSONObject columnJson : columns) {
            if ("操作".equals(columnJson.getString("label"))) {
                // 添加删除按钮
                columnJson.getJSONArray("buttons").add(deleteJsonObj);
            }
        }
        // 重新写入
        JSONUtil.write(this, "$.body.columns", columns);
    }

    public void addCrudColumnButtonInfo(Class<?> clazz) {
        List<Field> buttonFields = AnnoUtil.getAnnoButtonFields(clazz);
        // 读取现有的列
        List<JSONObject> columns = JSONUtil.readList(this, "$.body.columns", JSONObject.class);
        for (JSONObject columnJson : columns) {
            if ("操作".equals(columnJson.getString("label"))) {
                for (Field buttonField : buttonFields) {
                    AnnoButton annoButton = AnnotationUtil.getAnnotation(buttonField, AnnoButton.class);
                    JSONObject buttonJson = new JSONObject();
                    AnnoButton.O2MJoinButton o2MJoinButton = annoButton.o2mJoinButton();
                    AnnoButton.M2MJoinButton m2mJoinButton = annoButton.m2mJoinButton();
                    if (o2MJoinButton.enable()) {
                        buttonJson.put("label", annoButton.name());
                        buttonJson.put("type", "button");
                        buttonJson.put("actionType", "dialog");
                        buttonJson.put("dialog", new JSONObject() {{
                            put("size", "full");
                            put("title", annoButton.name());
                            put("body", new JSONObject() {{
                                put("type", "iframe");
                                put("src", "/system/config/amis/" + o2MJoinButton.joinAnnoMainClazz().getSimpleName() + "?" + o2MJoinButton.joinOtherClazzField() + "=${" + o2MJoinButton.joinThisClazzField() + "}");
                            }});
                        }});
                    } else if (m2mJoinButton.enable()) {
                        HashMap<String, Object> queryMap = new HashMap<String, Object>() {{
                            put("joinValue", "${" + m2mJoinButton.joinThisClazzField() + "}");
                            put("joinCmd", Base64.encodeStr(m2mJoinButton.joinSql().getBytes(), false, true));
                            put("mediumThisField", m2mJoinButton.mediumThisField());
                            put("mediumOtherField", m2mJoinButton.mediumOtherField());
                            put("mediumTableClass", m2mJoinButton.mediumTableClass().getSimpleName());
                            put("joinThisClazzField", m2mJoinButton.joinThisClazzField());
                        }};
                        buttonJson.put("label", annoButton.name());
                        buttonJson.put("type", "button");
                        buttonJson.put("actionType", "drawer");
                        buttonJson.put("drawer", new JSONObject() {{
                            put("size", "xl");
                            put("title", annoButton.name());
                            put("showCloseButton", false);
                            put("actions", new JSONArray() {{
                                add(new JSONObject() {{
                                    put("type", "action");
                                    put("actionType", "confirm");
                                    put("label", "关闭");
                                    put("size", "lg");
                                    put("level", "primary");
                                }});
                            }});
                            put("body", new JSONObject() {{
                                put("type", "iframe");
                                put("src", "/system/config/amis-m2m/" + m2mJoinButton.joinAnnoMainClazz().getSimpleName() + "?" + URLUtil.buildQuery(queryMap, null));
                            }});
                        }});
                    } else if (StrUtil.isNotBlank(annoButton.jumpUrl())) {
                        buttonJson.put("label", annoButton.name());
                        buttonJson.put("type", "button");
                        buttonJson.put("actionType", "url");
                        buttonJson.put("url", annoButton.jumpUrl());
                    } else if (StrUtil.isNotBlank(annoButton.jsCmd())) {
                        buttonJson.put("label", annoButton.name());
                        buttonJson.put("type", "button");
                        buttonJson.put("onClick", annoButton.jsCmd());
                    } else if(annoButton.javaCmd().enable()){
                        buttonJson.put("label", annoButton.name());
                        buttonJson.put("type", "button");
                        buttonJson.put("actionType", "ajax");
                        buttonJson.put("api",new HashMap<String ,Object >(){{
                            put("method","post");
                            put("url","/system/anno/runJavaCmd");
                            put("data",new HashMap<String ,String >(){{
                                put("clazz", CryptoUtil.encrypt(annoButton.javaCmd().beanClass().getName()));
                                put("method",CryptoUtil.encrypt(annoButton.javaCmd().methodName()));
                                put("&", "$$");
                            }});
                            put("messages",new HashMap<String,Object>(){{
                                put("success","操作成功");
                                put("failed","操作失败");
                            }});
                        }});
                    } else {
                        continue;
                    }
                    // 添加对应按钮
                    columnJson.getJSONArray("buttons").add(buttonJson);
                    // 设置列宽
                    columnJson.put("width", columnJson.getJSONArray("buttons").size() * 80);
                }
            }
        }
        // 重新写入
        JSONUtil.write(this, "$.body.columns", columns);
    }


    //---------------------- 以下为CRUD的树代码 ----------------------//

    public void addTreeForm(Class<?> clazz) {
        AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
        String parentKey = annoMain.annoTree().parentKey();

        List<Field> fields = AnnoUtil.getAnnoFields(clazz);
        ArrayList<FormItem> itemList = CollUtil.newArrayList();
        for (Field field : fields) {
            AnnoField annoField = field.getAnnotation(AnnoField.class);
            PrimaryKey annoId = field.getAnnotation(PrimaryKey.class);
            boolean required = annoField.edit().notNull();
            String fieldName = field.getName();
            FormItem formItem = new FormItem();
            formItem.setRequired(required);
            formItem.setName(fieldName);
            formItem.setLabel(annoField.title());
            formItem = AnnoDataType.editorExtraInfo(formItem, annoField);
            if (annoId != null) {
                formItem.setDisabled(true);
            }
            if (!annoField.show()) {
                formItem.setHidden(true);
            }
            if (parentKey.equals(fieldName)) {
                formItem = new TreeSelect();
                formItem.setId("parent-tree-select");
                formItem.setRequired(required);
                formItem.setName(fieldName);
                formItem.setLabel(annoField.title());
                ((TreeSelect)formItem).setSource(
                        new Api(){{
                            setMethod("get");
                            setUrl("/system/anno/${treeClazz}/annoTrees");
                        }}
                );
            }
            itemList.add(formItem);
        }
        JSONUtil.write(this, "$.body[1].body", itemList);

        // 设置${_parentKey}的值
        String parentPk = AnnoUtil.getParentPk(clazz);
        JSONUtil.write(this, "$.body[0].buttons[1].onEvent.click.actions[1].args.value", new JSONObject() {{
            put(parentPk, "${_cat}");
        }});
    }

    //---------------------- 以下为CRUD的多对多关系代码 ----------------------//

    public void addDeleteRelationEditInfo(Class<?> clazz) {
        // 删除按钮模板
        JSONObject deleteJsonObj = new JSONObject() {{
            put("type", "button");
            put("actionType", "ajax");
            put("level", "danger");
            put("label", "删除关联关系");
            put("confirmText", "您确认要删除关联关系吗?");
            put("api", new JSONObject() {{
                put("method", "post");
                put("url", "/system/anno/${clazz}/remove-relation");
                put("data", new JSONObject() {{
                    put("&", "$$");
                    put("_extraData", "${extraData}");
                }});
                put("messages", new JSONObject() {{
                    put("success", "操作成功");
                    put("failed", "操作失败");
                }});
            }});
        }};
        // 读取现有的列
        List<JSONObject> columns = JSONUtil.readList(this, "$.body.columns", JSONObject.class);
        for (JSONObject columnJson : columns) {
            if ("操作".equals(columnJson.getString("label"))) {
                // 添加删除按钮
                columnJson.getJSONArray("buttons").add(deleteJsonObj);
            }
        }
        // 重新写入
        JSONUtil.write(this, "$.body.columns", columns);
    }

    /**
     * 添加crud关系数据
     *
     * @param clazz    clazz
     * @param amisJson ami json
     */
    public void addRelationCrudData(Class<?> clazz,Map<String ,Object> amisJson) {
        JSONUtil.write(this, "$.body.headerToolbar[2].dialog.body[0]", amisJson);
    }

}
