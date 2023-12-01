package site.sorghum.anno.amis.process.processer.crud;

import cn.hutool.core.collection.CollUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.display.Group;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.amis.model.CrudView;
import site.sorghum.anno.amis.process.BaseProcessor;
import site.sorghum.anno.amis.process.BaseProcessorChain;
import site.sorghum.anno.anno.enums.AnnoDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.hutool.core.collection.CollUtil.split;

/**
 * CRUD视图初始化处理器
 *
 * @author Sorghum
 * @since 2023/07/07
 */
@Named
public class CrudFilterProcessor implements BaseProcessor {

    @Inject
    MetadataManager metadataManager;

    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        CrudView crudView = (CrudView) amisBaseWrapper.getAmisBase();
        AnEntity anEntity = metadataManager.getEntity(clazz);

        // 获取过滤的模板
        Form form = createFilterForm(anEntity);

        // 写入到当前对象
        Crud crudBody = crudView.getCrudBody();
        crudBody.setFilter(form);
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }

    private Form createFilterForm(AnEntity anEntity) {
        Form form = new Form();
        form.setId("crud_filter");
        form.setTitle("条件搜索");
        form.setActions(createFilterActions());
        List<AmisBase> body = createFilterFormItems(anEntity);
        if (body.isEmpty()) {
            return form; // No filter fields, return an empty form
        }
        form.setBody(body);
        form.setOnEvent(createFilterEvent());

        // 设置默认排序数据
        Map<String, Object> data = form.getData();
        if (data == null) {
            data = new HashMap<>();
            form.setData(data);
        }
//        data.put("orderBy", anEntity.getOrderValue());
//        data.put("orderDir", anEntity.getOrderType());

        return form;
    }

    private List<Action> createFilterActions() {
        return CollUtil.newArrayList(
            createAction("submit", "primary", "搜索"),
            createAction("reset", null, "重置")
        );
    }

    private Action createAction(String type, String level, String label) {
        return new Action() {{
            setType(type);
            setLevel(level);
            setLabel(label);
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
}
