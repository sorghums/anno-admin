package site.sorghum.anno.modular.amis.process.processer.crud;

import cn.hutool.core.collection.CollUtil;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.wood.annotation.PrimaryKey;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.display.DialogButton;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno.metadata.AnEntity;
import site.sorghum.anno.metadata.AnField;
import site.sorghum.anno.metadata.MetadataManager;
import site.sorghum.anno.modular.amis.model.CrudView;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;
import site.sorghum.anno.modular.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
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
public class CrudAddInfoProcessor implements BaseProcessor {

    @Inject
    MetadataManager metadataManager;

    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain){
        CrudView crudView = (CrudView) amisBaseWrapper.getAmisBase();
        // 判断是否可以编辑
        AnEntity anEntity = metadataManager.getEntity(clazz);
        List<Field> annoFields = AnnoUtil.getAnnoFields(clazz);
        List<AnField> anFields = anEntity.getFields();
        boolean canAdd = anFields.stream().anyMatch(AnField::isAddEnable);
        if (!canAdd) {
            return;
        }
        List<AmisBase> formItems = new ArrayList<AmisBase>() {{
            for (AnField field : anFields) {
                if (field.isPrimaryKey()) {
                    add(new FormItem() {{
                        setName(field.getFieldName());
                        setType("hidden");
                    }});
                    continue;
                }
                if (field.isAddEnable()) {
                    FormItem formItem = new FormItem();
                    formItem.setName(field.getFieldName());
                    formItem.setLabel(field.getTitle());
                    formItem.setRequired(field.isEditNotNull());
                    formItem.setPlaceholder(field.getEditPlaceHolder());
                    formItem = AnnoDataType.editorExtraInfo(formItem, field);
                    add(formItem);
                }
            }
        }};
        Crud crudBody = crudView.getCrudBody();
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
                                setApi(new Api() {{
                                    setMethod("post");
                                    setUrl("/system/anno/${clazz}/save");
                                }});
                                setId("simple-add-form");
                                setSize("lg");
                                setBody(formItems);
                                // 刷新某个组件
                                setOnEvent(new HashMap<>() {{
                                    put("submitSucc", new HashMap<>() {{
                                        put("actions",
                                                CollUtil.newArrayList(new HashMap<>() {{
                                                                          put("actionType", "reload");
                                                                          put("componentId", "crud_template_main");
                                                                      }}
                                                ));
                                    }});
                                }});
                            }}
                    );
                }}
        );
        actions.add(0, dialogButton);
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
