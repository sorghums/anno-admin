package site.sorghum.anno.modular.amis.process.processer.tree;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import org.noear.solon.annotation.Component;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.DrawerButton;
import site.sorghum.amis.entity.display.IFrame;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.AnAmis;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.anno.modular.amis.model.TreeView;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;
import site.sorghum.anno.modular.anno.annotation.field.AnnoButton;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import site.sorghum.anno.common.util.CryptoUtil;

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
@Component
public class TreeColumnButtonProcessor implements BaseProcessor {
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        TreeView treeView = (TreeView) amisBaseWrapper.getAmisBase();
        List<Field> buttonFields = AnnoUtil.getAnnoButtonFields(clazz);
        // 读取现有的列
        Form formBody = treeView.getCrudForm();
        for (Field buttonField : buttonFields) {
            AnnoButton annoButton = AnnotationUtil.getAnnotation(buttonField, AnnoButton.class);
            Action action = new Action();
            AnnoButton.O2MJoinButton o2MJoinButton = annoButton.o2mJoinButton();
            AnnoButton.M2MJoinButton m2mJoinButton = annoButton.m2mJoinButton();
            if (o2MJoinButton.enable()) {
                action = new DrawerButton();
                action.setLabel(annoButton.name());
                ((DrawerButton) action).setDrawer(
                    new DrawerButton.Drawer() {{
                        setShowCloseButton(false);
                        setPosition("right");
                        setCloseOnOutside(true);
                        setSize("xl");
                        setHeaderClassName("p-none m-none h-0");
                        setFooterClassName("p-xs m-xs h-1/2");
                        setActions(new ArrayList<>());
                        setBody(
                            new IFrame() {{
                                setType("iframe");
                                setSrc("/amisSingle/index/" + o2MJoinButton.joinAnnoMainClazz().getSimpleName() + "?isM2m=true&" + o2MJoinButton.joinOtherClazzField() + "=${" + o2MJoinButton.joinThisClazzField() + "}");
                            }}
                        );
                    }}
                );
            } else if (m2mJoinButton.enable()) {
                action = new DrawerButton();
                HashMap<String, Object> queryMap = new HashMap<String, Object>() {{
                    put("joinValue", "${" + m2mJoinButton.joinThisClazzField() + "}");
                    put("joinCmd", Base64.encodeStr(m2mJoinButton.joinSql().getBytes(), false, true));
                    put("mediumThisField", m2mJoinButton.mediumThisField());
                    put("mediumOtherField", m2mJoinButton.mediumOtherField());
                    put("mediumTableClass", m2mJoinButton.mediumTableClass().getSimpleName());
                    put("joinThisClazzField", m2mJoinButton.joinThisClazzField());
                    put("isM2m", true);
                }};
                action.setLabel(annoButton.name());
                ((DrawerButton) action).setDrawer(
                    new DrawerButton.Drawer() {{
                        setCloseOnEsc(true);
                        setCloseOnOutside(true);
                        setSize("xl");
                        setShowCloseButton(false);
                        setHeaderClassName("p-none m-none h-0");
                        setFooterClassName("p-xs m-xs h-1/2");
                        setBody(
                            new IFrame() {{
                                setType("iframe");
                                setSrc("/amisSingle/index/" + o2MJoinButton.joinAnnoMainClazz().getSimpleName() + "?isM2m=true&" + o2MJoinButton.joinOtherClazzField() + "=${" + o2MJoinButton.joinThisClazzField() + "}");
                            }}
                        );
                        setActions(new ArrayList<Action>());
                    }}
                );
            } else if (StrUtil.isNotBlank(annoButton.jumpUrl())) {
                action.setLabel(annoButton.name());
                action.setActionType("url");
                action.setUrl(annoButton.jumpUrl());
            } else if (StrUtil.isNotBlank(annoButton.jsCmd())) {
                action.setLabel(annoButton.name());
                action.setOnClick(annoButton.jsCmd());
            } else if (annoButton.javaCmd().enable()) {
                action.setLabel(annoButton.name());
                action.setActionType("ajax");
                action.setApi(
                    new Api() {{
                        setMethod("post");
                        setUrl("/system/anno/runJavaCmd");
                        setData(new HashMap<String, Object>() {{
                            put("clazz", CryptoUtil.encrypt(annoButton.javaCmd().beanClass().getName()));
                            put("method", CryptoUtil.encrypt(annoButton.javaCmd().methodName()));
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
