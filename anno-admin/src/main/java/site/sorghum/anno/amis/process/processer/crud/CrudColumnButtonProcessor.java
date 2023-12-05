package site.sorghum.anno.amis.process.processer.crud;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.display.DialogButton;
import site.sorghum.amis.entity.display.IFrame;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.util.CryptoUtil;
import site.sorghum.anno._metadata.AnColumnButton;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.amis.model.CrudView;
import site.sorghum.anno.amis.process.BaseProcessor;
import site.sorghum.anno.amis.process.BaseProcessorChain;
import site.sorghum.anno.anno.proxy.PermissionProxy;

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
@Named
public class CrudColumnButtonProcessor implements BaseProcessor {

    @Inject
    MetadataManager metadataManager;

    @Inject
    PermissionProxy permissionProxy;

    private static final String OPERATION_LABEL = "操作";

    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        if (properties.getOrDefault("isM2m", false).equals(true)) {
            chain.doProcessor(amisBaseWrapper, clazz, properties);
            return;
        }
        CrudView crudView = (CrudView) amisBaseWrapper.getAmisBase();
        AnEntity anEntity = metadataManager.getEntity(clazz);
        Crud crudBody = crudView.getCrudBody();
        List<Map> columns = crudBody.getColumns();
        for (Map columnJson : columns) {
            if (OPERATION_LABEL.equals(MapUtil.getStr(columnJson, "label"))) {
                List<AnColumnButton> anColumnButtons = anEntity.getColumnButtons();
                for (AnColumnButton anColumnButton : anColumnButtons) {
                    try {
                        permissionProxy.checkPermission(anEntity, anColumnButton.getPermissionCode());
                    } catch (Exception e) {
                        continue;
                    }
                    Action action = createActionForButton(anColumnButton);
                    if (action != null) {
                        Object buttons = columnJson.get("buttons");
                        if (buttons instanceof List<?>) {
                            List<Object> buttonListMap = (List<Object>) buttons;
                            buttonListMap.add(action);
                            // 设置列宽
                            columnJson.put("width", buttonListMap.size() * 80);
                        }
                    }
                }
            }
        }
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }

    private Action createActionForButton(AnColumnButton anColumnButton) {
        Action action = null;
        if (anColumnButton.isO2mEnable()) {
            action = createO2MDialogButton(anColumnButton);
        } else if (anColumnButton.isM2mEnable()) {
            action = createM2mDialogButton(anColumnButton);
        } else if (StrUtil.isNotBlank(anColumnButton.getJumpUrl())) {
            action = createActionUrl(anColumnButton.getName(), anColumnButton.getJumpUrl());
        } else if (StrUtil.isNotBlank(anColumnButton.getJsCmd())) {
            action = createActionJsCmd(anColumnButton.getName(), anColumnButton.getJsCmd());
        } else if (anColumnButton.isJavaCmdEnable()) {
            action = createJavaCmdAction(anColumnButton);
        } else if (anColumnButton.isTplEnable()) {
            action = createTplDialogButton(anColumnButton);
        }
        return action;
    }

    private DialogButton createO2MDialogButton(AnColumnButton anColumnButton) {
        String label = anColumnButton.getName();
        String windowSize = anColumnButton.getO2mWindowSize();
        String src = AnnoConstants.BASE_URL + "/index.html#/amisSingle/index/" + anColumnButton.getO2mJoinMainClazz().getSimpleName()
                        + "?" + anColumnButton.getO2mJoinOtherField() + "=${" + anColumnButton.getO2mJoinThisField() + "}";
        DialogButton dialogButton = new DialogButton();
        dialogButton.setLabel(label);
        DialogButton.Dialog dialog = new DialogButton.Dialog();
        dialog.setTitle(label);
        dialog.setShowCloseButton(true);
        dialog.setSize(windowSize);
        dialog.setActions(new ArrayList<>());
        IFrame iFrame = new IFrame();
        iFrame.setHeight(anColumnButton.getO2mWindowHeight());
        iFrame.setSrc(src);
        dialog.setBody(iFrame);
        dialogButton.setDialog(dialog);
        return dialogButton;
    }

    private Action createM2mDialogButton(AnColumnButton anColumnButton) {
        DialogButton dialogButton = new DialogButton();
        dialogButton.setLabel(anColumnButton.getName());
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("joinValue", "${" + anColumnButton.getM2mJoinThisClazzField() + "}");
        queryMap.put("joinCmd", Base64.encodeStr(anColumnButton.getM2mJoinSql().getBytes(), false, true));
        queryMap.put("mediumThisField", anColumnButton.getM2mMediumTargetField());
        queryMap.put("mediumOtherField", anColumnButton.getM2mMediumThisField());
        queryMap.put("mediumTableClass", anColumnButton.getM2mMediumTableClass().getSimpleName());
        queryMap.put("joinThisClazzField", anColumnButton.getM2mJoinThisClazzField());
        queryMap.put("isM2m", true);

        DialogButton.Dialog dialog = new DialogButton.Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setTitle(anColumnButton.getName());
        dialog.setSize(anColumnButton.getM2mWindowSize());
        dialog.setShowCloseButton(true);
        IFrame iFrame = new IFrame();
        iFrame.setSrc(AnnoConstants.BASE_URL + "/index.html#/amisSingle/index/" + anColumnButton.getM2mJoinAnnoMainClazz().getSimpleName() + "?" + URLUtil.buildQuery(queryMap, null));
        iFrame.setHeight(anColumnButton.getM2mWindowHeight());
        dialog.setBody(iFrame);
        dialogButton.setDialog(dialog);
        dialog.setActions(new ArrayList<>());

        dialogButton.setDialog(dialog);
        return dialogButton;
    }

    private Action createActionUrl(String label, String url) {
        Action action = new Action();
        action.setLabel(label);
        action.setActionType("url");
        action.setUrl(url);
        return action;
    }

    private Action createActionJsCmd(String label, String jsCmd) {
        Action action = new Action();
        action.setLabel(label);
        action.setOnClick(jsCmd);
        return action;
    }

    private Action createJavaCmdAction(AnColumnButton anColumnButton) {
        Action action = new Action();
        action.setLabel(anColumnButton.getName());
        action.setActionType("ajax");
        Api api = new Api();
        api.setMethod("post");
        api.setUrl("/amis/system/anno/runJavaCmd");
        Map<String, Object> data = new HashMap<>();
        data.put("clazz", CryptoUtil.encrypt(anColumnButton.getJavaCmdBeanClass().getName()));
        data.put("method", CryptoUtil.encrypt(anColumnButton.getJavaCmdMethodName()));
        // 30分钟过期
        data.put("expireTime", CryptoUtil.encrypt(String.valueOf(System.currentTimeMillis() + 30 * 60 * 1000)));
        data.put("&", "$$");
        api.setData(data);
        Api.ApiMessage messages = new Api.ApiMessage();
        messages.setSuccess("操作成功");
        messages.setFailed("操作失败");
        api.setMessages(messages);
        action.setApi(api);
        return action;
    }

    private Action createTplDialogButton(AnColumnButton anColumnButton) {
        DialogButton dialogButton = new DialogButton();
        dialogButton.setLabel(anColumnButton.getName());
        DialogButton.Dialog dialog = new DialogButton.Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setTitle(anColumnButton.getName());
        dialog.setShowCloseButton(true);
        dialog.setSize(anColumnButton.getTplWindowSize());
        dialog.setActions(new ArrayList<>());
        IFrame iFrame = new IFrame();
        iFrame.setType("iframe");
        iFrame.setSrc("/annoTpl/" + anColumnButton.getTplClazz().getSimpleName() + "/" + anColumnButton.getTplName());
        iFrame.setHeight(anColumnButton.getTplWindowHeight());
        dialog.setBody(iFrame);
        dialogButton.setDialog(dialog);
        return dialogButton;
    }

}
