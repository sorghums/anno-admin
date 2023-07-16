package site.sorghum.anno.modular.amis.process.processer.crudm2m;

import cn.hutool.core.map.MapUtil;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.wood.annotation.PrimaryKey;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.display.DialogButton;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno.common.exception.BizException;
import site.sorghum.anno.metadata.AnEntity;
import site.sorghum.anno.metadata.AnField;
import site.sorghum.anno.metadata.MetadataManager;
import site.sorghum.anno.modular.amis.model.CrudM2mView;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;
import site.sorghum.anno.modular.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.anno.util.AnnoUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * crud-m2m编辑信息处理器
 *
 * @author Sorghum
 * @since 2023/07/10
 */
@Component
public class CrudM2mEditInfoProcessor implements BaseProcessor {

    @Inject
    MetadataManager metadataManager;
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        CrudM2mView crudM2mView = (CrudM2mView) amisBaseWrapper.getAmisBase();
        // 判断是否可以编辑
        AnEntity anEntity = metadataManager.getEntity(clazz);
        List<AnField> fields = anEntity.getFields();
        boolean canEdit = fields.stream().anyMatch(AnField::isEditEnable);
        if (!canEdit) {
            return;
        }
        Crud crudBody = crudM2mView.getCrudBody();
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
                for (AnField field : fields) {
                    if (field.isPrimaryKey()) {
                        add(new FormItem() {{
                            setName(field.getFieldName());
                            setType("hidden");
                        }});
                        continue;
                    }
                    if (field.isEditEnable()) {
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
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
