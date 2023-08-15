package site.sorghum.anno.amis.process.processer.crudm2m;

import cn.hutool.core.map.MapUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.display.DialogButton;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.amis.model.CrudM2mView;
import site.sorghum.anno.amis.process.BaseProcessor;
import site.sorghum.anno.amis.process.BaseProcessorChain;
import site.sorghum.anno.anno.enums.AnnoDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * crud-m2m编辑信息处理器
 *
 * @author Sorghum
 * @since 2023/07/10
 */
@Named
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
                    FormItem formItem = new FormItem();
                    formItem.setName(field.getFieldName());
                    formItem.setLabel(field.getTitle());
                    formItem.setRequired(field.isEditNotNull());
                    formItem.setPlaceholder(field.getEditPlaceHolder());
                    formItem = AnnoDataType.editorExtraInfo(formItem, field);
                    if (!field.isEditEnable()) {
                        formItem.setHidden(true);
                    }
                    add(formItem);

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
                                setUrl("/amis/system/anno/${clazz}/updateById");
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
