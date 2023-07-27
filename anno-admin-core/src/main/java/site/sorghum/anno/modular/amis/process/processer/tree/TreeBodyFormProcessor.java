package site.sorghum.anno.modular.amis.process.processer.tree;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.function.ButtonGroup;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.input.TreeSelect;
import site.sorghum.anno.common.util.JSONUtil;
import site.sorghum.anno.metadata.AnEntity;
import site.sorghum.anno.metadata.AnField;
import site.sorghum.anno.metadata.MetadataManager;
import site.sorghum.anno.modular.amis.model.TreeView;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.anno.util.AnnoUtil;

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
    @Inject
    MetadataManager metadataManager;
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        TreeView treeView = (TreeView) amisBaseWrapper.getAmisBase();
        AnEntity anEntity = metadataManager.getEntity(clazz);
        String parentKey = anEntity.getTreeParentKey();
        List<AnField> fields = anEntity.getFields();
        ArrayList<AmisBase> itemList = new ArrayList<>();
        for (AnField field : fields) {
            boolean required = field.isEditNotNull();
            String fieldName = field.getFieldName();
            FormItem formItem = new FormItem();
            formItem.setRequired(required);
            formItem.setName(fieldName);
            formItem.setLabel(field.getTitle());
            formItem = AnnoDataType.editorExtraInfo(formItem, field);
            if (field.isPrimaryKey()) {
                formItem.setDisabled(true);
            }
            if (!field.isShow()) {
                formItem.setHidden(true);
            }
            if (parentKey.equals(fieldName)) {
                formItem = new TreeSelect();
                formItem.setId("parent-tree-select");
                formItem.setRequired(required);
                formItem.setName(fieldName);
                formItem.setLabel(field.getTitle());
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
