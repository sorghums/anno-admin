package site.sorghum.anno.modular.amis.process.processer.crud;

import cn.hutool.core.collection.CollUtil;
import org.noear.solon.annotation.Component;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.display.Group;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno.modular.amis.model.CrudView;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
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
 * CRUD视图初始化处理器
 *
 * @author Sorghum
 * @since 2023/07/07
 */
@Component
public class CrudFilterProcessor implements BaseProcessor {
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain){
        CrudView crudView = (CrudView) amisBaseWrapper.getAmisBase();
        AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
        // 获取过滤的模板
        Form form = new Form();
        form.setId("crud_filter");
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
        form.setOnEvent(
                new HashMap<>() {{
                    put("broadcast_aside_change",
                            new HashMap<>(){{
                                put("actions",CollUtil.newArrayList(new HashMap<>() {{
                                                                        put("actionType", "reload");
                                                                        put("componentId", "crud_template_main");
                                                                    }}
                                ));
                            }}
                    );
                }}
        );
        // 设置默认排序数据
        Map<String, Object> data = form.getData();
        if (data == null) {
            data = new HashMap<>();
            form.setData(data);
        }
        data.put("orderBy",annoMain.annoOrder().orderValue());
        data.put("orderDir", annoMain.annoOrder().orderType());
        // 写入到当前对象
        Crud crudBody = crudView.getCrudBody();
        crudBody.setFilter(form);
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}