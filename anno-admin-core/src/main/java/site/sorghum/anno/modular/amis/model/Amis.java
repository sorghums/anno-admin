package site.sorghum.anno.modular.amis.model;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import site.sorghum.anno.exception.BizException;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoLeftTree;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.modular.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import site.sorghum.anno.modular.anno.util.TemplateUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Amis
 *
 * @author sorghum
 * @since 2023/05/20
 */
public class Amis extends JSONObject {

    public static <T> T read(Object obj, String path, Class<T> type) {
        Object eval = JSONPath.eval(obj, path);
        if (eval instanceof JSONArray) {
            throw new BizException("类型不匹配");
        }
        if (eval instanceof JSONObject) {
            JSONObject evalJson = (JSONObject) eval;
            return evalJson.toJavaObject(type);
        }
        return JSONObject.parseObject(JSON.toJSONString(obj), type);
    }

    public static <T> List<T> readList(Object obj, String path, Class<T> type) {
        Object eval = JSONPath.eval(obj, path);
        if (eval instanceof JSONArray) {
            JSONArray evalArray = (JSONArray) eval;
            return evalArray.toJavaList(type);
        }
        if (eval instanceof JSONObject) {
            throw new BizException("类型不匹配");
        }
        return JSONArray.parseArray(JSON.toJSONString(eval), type);
    }

    public static Object write(Object obj, String path, Object value) {
        return JSONPath.set(obj, path, value);
    }

    /**
     * 添加树边栏
     *
     * @param clazz clazz
     */
    public void addCommonTreeAside(Class<?> clazz) {
        AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
        AnnoLeftTree annoLeftTree = annoMain.annoLeftTree();
        if (annoLeftTree.enable()) {
            JSONObject aside = TemplateUtil.getTemplate("item/aside.json");
            Amis.write(this, "$.aside", aside);
            return;
        }
        AnnoTree annoTree = annoMain.annoTree();
        if (annoTree.enable() && annoTree.displayAsTree()) {
            JSONObject aside = TemplateUtil.getTemplate("item/aside.json");
            Amis.write(this, "$.aside", aside);
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
        JSONObject filter = TemplateUtil.getTemplate("item/filter.json");
        List<JSONObject> body = Amis.readList(filter, "$.body", JSONObject.class);
        List<Field> fields = AnnoUtil.getAnnoFields(clazz);
        List<JSONObject> amisColumns = new ArrayList<>();
        for (Field field : fields) {
            AnnoField annoField = field.getAnnotation(AnnoField.class);
            AnnoSearch search = annoField.search();
            if (search.enable()) {
                JSONObject column = new JSONObject() {{
                    put("name", field.getName());
                    put("label", annoField.title());
                    put("clearable", true);
                    put("placeholder", search.placeHolder());
                    put("size", "sm");
                }};
                AnnoDataType.editorExtraInfo(column, annoField);
                amisColumns.add(column);
            }
        }
        // amisColumns 以4个为一组进行分组
        CollUtil.split(amisColumns, 4).forEach(columns -> {
            JSONObject group = new JSONObject() {{
                put("type", "group");
                put("body", columns);
            }};
            body.add(group);
        });
        // 重新写入
        Amis.write(filter, "$.body", body);
        // 写入到当前对象
        Amis.write(this, "$.body.filter", filter);
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
            amisColumn.put("width", 200);
            amisColumn.put("sortable", true);
            AnnoDataType.displayExtraInfo(amisColumn, annoField);
            if (!annoField.show()) {
                amisColumn.put("toggled",false);
            }
            amisColumns.add(amisColumn);
        }
        // 读取现有的列
        List<JSONObject> columns = Amis.readList(this, "$.body.columns", JSONObject.class);
        columns.addAll(0, amisColumns);
        // 重新写入
        Amis.write(this, "$.body.columns", columns);
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
        List<JSONObject> columns = Amis.readList(this, "$.body.columns", JSONObject.class);
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
                            if (annoField.isId()) {
                                add(new JSONObject() {{
                                    put("name", field.getName());
                                    put("type", "hidden");
                                }});
                                continue;
                            }
                            AnnoEdit edit = annoField.edit();
                            if (edit.editEnable()) {
                                JSONObject editItem = new JSONObject() {{
                                    put("name", field.getName());
                                    put("label", annoField.title());
                                    put("required", edit.notNull());
                                    put("placeholder", edit.placeHolder());
                                }};
                                AnnoDataType.editorExtraInfo(editItem, annoField);
                                add(editItem);
                            }
                        }
                    }});
                }});

            }});
        }});
        Amis.write(this, "$.body.columns", columns);
    }

    public void addCrudAddInfo(Class<?> clazz) {
        // 判断是否可以编辑
        List<Field> annoFields = AnnoUtil.getAnnoFields(clazz);
        boolean canAdd = annoFields.stream().map(f -> f.getAnnotation(AnnoField.class)).anyMatch(annoField -> annoField.edit().addEnable());
        if (!canAdd) {
            return;
        }
        List<JSONObject> action = Amis.readList(this, "$.body.filter.actions", JSONObject.class);
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
                    put("name", "simple-edit-form");
                    put("api", "post:/system/anno/${clazz}/save");
                    put("controls", new JSONArray() {{
                        List<Field> fields = AnnoUtil.getAnnoFields(clazz);
                        for (Field field : fields) {
                            AnnoField annoField = field.getAnnotation(AnnoField.class);
                            if (annoField.isId()) {
                                add(new JSONObject() {{
                                    put("name", field.getName());
                                    put("type", "hidden");
                                }});
                                continue;
                            }
                            AnnoEdit edit = annoField.edit();
                            if (edit.addEnable()) {
                                JSONObject item = new JSONObject() {{
                                    put("name", field.getName());
                                    put("label", annoField.title());
                                    put("required", edit.notNull());
                                    put("placeholder", edit.placeHolder());
                                }};
                                AnnoDataType.displayExtraInfo(item, annoField);
                                add(item);
                            }
                        }
                    }});
                }});

            }});
        }});
        Amis.write(this, "$.body.filter.actions", action);
    }

    public void addCrudDeleteButton(Class<?> clazz) {
        // 删除按钮模板
        String deleteJson = " {\n" +
                "                    \"type\": \"button\",\n" +
                "                    \"actionType\": \"ajax\",\n" +
                "                    \"level\": \"danger\",\n" +
                "                    \"label\": \"删除\",\n" +
                "                    \"confirmText\": \"您确认要删除?\",\n" +
                "                    \"api\": \"post:/system/anno/${clazz}/removeById\"\n" +
                "                  }";
        // 读取现有的列
        List<JSONObject> columns = Amis.readList(this, "$.body.columns", JSONObject.class);
        for (JSONObject columnJson : columns) {
            if ("操作".equals(columnJson.getString("label"))) {
                // 添加删除按钮
                columnJson.getJSONArray("buttons").add(JSON.parse(deleteJson));
            }
        }
        // 重新写入
        Amis.write(this, "$.body.columns", columns);
    }

    public void addTreeForm(Class<?> clazz) {
        AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
        String parentKey = annoMain.annoTree().parentKey();

        List<Field> fields = AnnoUtil.getAnnoFields(clazz);
        ArrayList<JSONObject> itemList = CollUtil.newArrayList();
        for (Field field : fields) {
            AnnoField annoField = field.getAnnotation(AnnoField.class);
            boolean required = annoField.edit().notNull();
            String fieldName = field.getName();
            JSONObject itemBody = new JSONObject() {{
                put("name", fieldName);
                put("label", annoField.title());
                put("required", required);
            }};
            AnnoDataType.editorExtraInfo(itemBody, annoField);
            if (annoField.isId()){
                itemBody.put("disabled",true);
            }
            if (!annoField.show()){
                itemBody.put("hidden",true);
            }
            if (parentKey.equals(fieldName)){
                itemBody = TemplateUtil.getTemplate("item/tree-select.json");
                itemBody.put("name", fieldName);
                itemBody.put("label", annoField.title());
            }
            itemList.add(itemBody);
        }
        Amis.write(this, "$.body[1].body", itemList);

        // 设置${_parentKey}的值
        String parentPk = AnnoUtil.getParentPk(clazz);
        Amis.write(this, "$.body[0].buttons[1].onEvent.click.actions[1].args.value", new JSONObject(){{
            put(parentPk,"${_cat}");
        }});
    }
}
