package site.sorghum.anno.modular.amis.process.processer.tree;

import org.noear.solon.annotation.Component;
import org.noear.wood.annotation.PrimaryKey;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.function.ButtonGroup;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.input.TreeSelect;
import site.sorghum.anno.modular.amis.model.TreeView;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import site.sorghum.anno.util.JSONUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树表单处理器
 *
 * @author Sorghum
 * @since 2023/07/10
 */
@Component
public class TreeBodyFormProcessor implements BaseProcessor {
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        TreeView treeView = (TreeView) amisBaseWrapper.getAmisBase();
        AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
        String parentKey = annoMain.annoTree().parentKey();

        List<Field> fields = AnnoUtil.getAnnoFields(clazz);
        ArrayList<AmisBase> itemList = new ArrayList<>();
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
                ((TreeSelect) formItem).setSource(
                        new Api() {{
                            setMethod("get");
                            setUrl("/system/anno/${treeClazz}/annoTrees");
                        }}
                );
            }
            itemList.add(formItem);
        }
        Form crudForm = treeView.getCrudForm();
        crudForm.setBody(itemList);
        ButtonGroup crudButtonGroup =treeView.getCrudButtonGroup();
        List<Action> actions = crudButtonGroup.getButtons();
        Action action = actions.get(1);
        Map<String, Object> onEvent =
                action.getOnEvent();
        // 设置${_parentKey}的值
        String parentPk = AnnoUtil.getParentPk(clazz);
        JSONUtil.write(onEvent, "$.click.actions[1].args.value", new HashMap<>() {{
            put(parentPk, "${_cat}");
        }});
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
