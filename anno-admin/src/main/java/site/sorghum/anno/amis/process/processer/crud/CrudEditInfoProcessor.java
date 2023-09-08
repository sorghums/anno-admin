package site.sorghum.anno.amis.process.processer.crud;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.URLUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.display.DialogButton;
import site.sorghum.amis.entity.display.IFrame;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.layout.Tabs;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._metadata.AnColumnButton;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.amis.model.CrudView;
import site.sorghum.anno.amis.process.BaseProcessor;
import site.sorghum.anno.amis.process.BaseProcessorChain;
import site.sorghum.anno.amis.util.AmisCommonUtil;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.proxy.PermissionProxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CRUD视图行编辑按钮处理器
 *
 * @author Sorghum
 * @since 2023/07/07
 */
@Named
public class CrudEditInfoProcessor implements BaseProcessor {

    @Inject
    MetadataManager metadataManager;

    @Inject
    PermissionProxy permissionProxy;

    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        AnEntity entity = metadataManager.getEntity(clazz);

        CrudView crudView = (CrudView) amisBaseWrapper.getAmisBase();
        // 判断是否可以编辑
        List<AnField> fields = entity.getFields();
        boolean canEdit = fields.stream().anyMatch(AnField::isEditEnable);
        if (!canEdit) {
            chain.doProcessor(amisBaseWrapper, clazz, properties);
            return;
        }
        Crud crudBody = crudView.getCrudBody();
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
                    // 单独设置label宽度
                    formItem.setLabelWidth(formItem.getLabel().length() * 14);
                    formItem.setRequired(field.isEditNotNull());
                    formItem.setPlaceholder(field.getEditPlaceHolder());
                    formItem = AnnoDataType.editorExtraInfo(formItem, field);
                    if (!field.isEditEnable()) {
                        formItem.setHidden(true);
                    }
                    add(formItem);
                }
            }};

            // 一对多 多对多相关按钮
            Tabs tabs = new Tabs();
            processEditTabs(entity,tabs);
            formItems.add(tabs);
            dialogButton.setDialog(
                new DialogButton.Dialog() {{
                    setTitle("编辑");
                    setBody(
                        new Form() {{
                            setId("simple-edit-form");
                            setWrapWithPanel(false);
                            setSize("lg");
                            setApi(new Api() {{
                                setMethod("post");
                                setUrl("/amis/system/anno/${clazz}/updateById");
                            }});
                            setMode("horizontal");
                            setHorizontal(new FormHorizontal() {{
                                setRightFixed("sm");
                                setJustify(true);
                            }});
                            setBody(AmisCommonUtil.formItemToGroup(formItems, 2));
                        }}
                    );
                }}
            );
            buttonListMap.add(dialogButton);
        }
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }

    private void processEditTabs(AnEntity anEntity,Tabs tabs) {
        List<AnColumnButton> anColumnButtons = anEntity.getColumnButtons();
        for (AnColumnButton anColumnButton : anColumnButtons) {
            try {
                permissionProxy.checkPermission(anEntity, anColumnButton.getPermissionCode());
            } catch (Exception e) {
                continue;
            }
            if (anColumnButton.isO2mEnable()) {
                Tabs.Tab tab = new Tabs.Tab();
                tab.setTitle(anColumnButton.getName());
                tab.setBody(List.of(new IFrame() {{
                    setType("iframe");
                    setHeight(anColumnButton.getO2mWindowHeight());
                    setSrc("/index.html#/amisSingle/index/" + anColumnButton.getO2mJoinMainClazz().getSimpleName() + "?" + anColumnButton.getO2mJoinOtherField() + "=${" + anColumnButton.getO2mJoinThisField() + "}");
                }}));
                tabs.getTabs().add(tab);
            } else if (anColumnButton.isM2mEnable()) {
                Tabs.Tab tab = new Tabs.Tab();
                HashMap<String, Object> queryMap = new HashMap<>() {{
                    put("joinValue", "${" + anColumnButton.getM2mJoinThisClazzField() + "}");
                    put("joinCmd", Base64.encodeStr(anColumnButton.getM2mJoinSql().getBytes(), false, true));
                    // 处理上调换this和other的逻辑
                    put("mediumThisField", anColumnButton.getM2mMediumOtherField());
                    put("mediumOtherField", anColumnButton.getM2mMediumThisField());
                    put("mediumTableClass", anColumnButton.getM2mMediumTableClass().getSimpleName());
                    put("joinThisClazzField", anColumnButton.getM2mJoinThisClazzField());
                    put("isM2m", true);
                }};
                tab.setTitle(anColumnButton.getName());
                tab.setBody(List.of(new IFrame() {{
                    setType("iframe");
                    setHeight(anColumnButton.getM2mWindowHeight());
                    setSrc("/index.html#/amisSingle/index/" + anColumnButton.getM2mJoinAnnoMainClazz().getSimpleName() + "?" + URLUtil.buildQuery(queryMap, null));
                }}));
                tabs.getTabs().add(tab);
            } else {
                continue;
            }

        }
    }
}
