package site.sorghum.anno.amis.process.processer.tree;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.DialogButton;
import site.sorghum.amis.entity.display.DrawerButton;
import site.sorghum.amis.entity.display.IFrame;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.anno.amis.model.TreeView;
import site.sorghum.anno.amis.process.BaseProcessor;
import site.sorghum.anno.amis.process.BaseProcessorChain;
import site.sorghum.anno._common.util.CryptoUtil;
import site.sorghum.anno._metadata.AnColumnButton;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.proxy.PermissionProxy;
import site.sorghum.anno.anno.util.AnnoUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树列按钮处理器
 *
 * @author Sorghum
 * @since 2023/07/10
 */
@Named
public class TreeColumnButtonProcessor implements BaseProcessor {

    @Inject
    MetadataManager metadataManager;

    @Inject
    PermissionProxy permissionProxy;

    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        TreeView treeView = (TreeView) amisBaseWrapper.getAmisBase();
        AnEntity anEntity = metadataManager.getEntity(clazz);
        List<Field> buttonFields = AnnoUtil.getAnnoButtonFields(clazz);
        // 读取现有的列
        Form formBody = treeView.getCrudForm();
        List<AnColumnButton> anColumnButtons = anEntity.getColumnButtons();
        for (AnColumnButton anColumnButton : anColumnButtons) {
            try {
                permissionProxy.checkPermission(anEntity, anColumnButton.getPermissionCode());
            } catch (Exception e) {
                continue;
            }
            Action action = new Action();
            if (anColumnButton.isO2mEnable()) {
                action = new DialogButton();
                action.setLabel(anColumnButton.getName());
                ((DialogButton) action).setDialog(
                    new DialogButton.Dialog() {{
                        setTitle(anColumnButton.getName());
                        setShowCloseButton(true);
                        setSize(anColumnButton.getO2mWindowSize());
                        setActions(new ArrayList<>());
                        setBody(
                            new IFrame() {{
                                setType("iframe");
                                setHeight(anColumnButton.getO2mWindowHeight());
                                setSrc("/index#/amisSingle/index/" + anColumnButton.getO2mJoinMainClazz().getSimpleName() + "?" + anColumnButton.getO2mJoinOtherField() + "=${" + anColumnButton.getO2mJoinThisField() + "}");
                            }}
                        );
                    }}
                );
            } else if (anColumnButton.isM2mEnable()) {
                action = new DialogButton();
                HashMap<String, Object> queryMap = new HashMap<>() {{
                    put("joinValue", "${" + anColumnButton.getM2mJoinThisClazzField() + "}");
                    put("joinCmd", Base64.encodeStr(anColumnButton.getM2mJoinSql().getBytes(), false, true));
                    put("mediumThisField", anColumnButton.getM2mMediumOtherField());
                    put("mediumOtherField", anColumnButton.getM2mMediumThisField());
                    put("mediumTableClass", anColumnButton.getM2mMediumTableClass().getSimpleName());
                    put("joinThisClazzField", anColumnButton.getM2mJoinThisClazzField());
                    put("isM2m", true);
                }};
                action.setLabel(anColumnButton.getName());
                ((DialogButton) action).setDialog(
                    new DialogButton.Dialog() {{
                        setTitle(anColumnButton.getName());
                        setCloseOnEsc(true);
                        setSize(anColumnButton.getM2mWindowSize());
                        setShowCloseButton(true);
                        setBody(
                            new IFrame() {{
                                setType("iframe");
                                setHeight(anColumnButton.getM2mWindowHeight());
                                setSrc("/index#/amisSingle/index/" + anColumnButton.getM2mJoinAnnoMainClazz().getSimpleName() + "?isM2m=true&" + anColumnButton.getM2mMediumOtherField() + "=${" + anColumnButton.getM2mJoinThisClazzField() + "}");
                            }}
                        );
                        setActions(new ArrayList<>());
                    }}
                );
            } else if (StrUtil.isNotBlank(anColumnButton.getJumpUrl())) {
                action.setLabel(anColumnButton.getName());
                action.setActionType("url");
                action.setUrl(anColumnButton.getJumpUrl());
            } else if (StrUtil.isNotBlank(anColumnButton.getJsCmd())) {
                action.setLabel(anColumnButton.getName());
                action.setOnClick(anColumnButton.getJsCmd());
            } else if (anColumnButton.isJavaCmdEnable()) {
                action.setLabel(anColumnButton.getName());
                action.setActionType("ajax");
                action.setApi(
                    new Api() {{
                        setMethod("post");
                        setUrl("/system/anno/runJavaCmd");
                        setData(new HashMap<>() {{
                            put("clazz", CryptoUtil.encrypt(anColumnButton.getJavaCmdBeanClass().getName()));
                            put("method", CryptoUtil.encrypt(anColumnButton.getJavaCmdMethodName()));
                            // 30分钟过期
                            put("expireTime", CryptoUtil.encrypt(String.valueOf(System.currentTimeMillis() + 30 * 60 * 1000)));
                            put("&", "$$");
                        }});
                        setMessages(
                            new ApiMessage() {{
                                setSuccess("操作成功");
                                setFailed("操作失败，请刷新页面后重试。");
                            }}
                        );

                    }}
                );
            } else {
                continue;
            }
            action.setHiddenOn("!_hasId");
            formBody.getActions().add(action);
        }
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
