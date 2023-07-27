package site.sorghum.anno.modular.amis.process.processer.crud;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import org.noear.solon.annotation.Component;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.display.DrawerButton;
import site.sorghum.amis.entity.display.IFrame;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.anno.modular.amis.model.CrudView;
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
 * CRUD视图初始化处理器
 *
 * @author Sorghum
 * @since 2023/07/07
 */
@Component
public class CrudColumnButtonProcessor implements BaseProcessor {
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        if (properties.getOrDefault("isM2m", false).equals(true)) {
            chain.doProcessor(amisBaseWrapper, clazz, properties);
            return;
        }
        CrudView crudView = (CrudView) amisBaseWrapper.getAmisBase();
        List<Field> buttonFields = AnnoUtil.getAnnoButtonFields(clazz);
        // 读取现有的列
        Crud crudBody = crudView.getCrudBody();
        List<Map> columns = crudBody.getColumns();
        for (Map columnJson : columns) {
            if ("操作".equals(MapUtil.getStr(columnJson, "label"))) {
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
                                        setSrc("/#/amisSingle/index/" + o2MJoinButton.joinAnnoMainClazz().getSimpleName() + "?isM2m=true&" + o2MJoinButton.joinOtherClazzField() + "=${" + o2MJoinButton.joinThisClazzField() + "}");
                                    }}
                                );
                            }}
                        );
                    } else if (m2mJoinButton.enable()) {
                        action = new DrawerButton();
                        HashMap<String, Object> queryMap = new HashMap<String, Object>() {{
                            put("joinValue", "${" + m2mJoinButton.joinThisClazzField() + "}");
                            put("joinCmd", Base64.encodeStr(m2mJoinButton.joinSql().getBytes(), false, true));
                            // 处理上调换this和other的逻辑
                            put("mediumThisField", m2mJoinButton.mediumOtherField());
                            put("mediumOtherField", m2mJoinButton.mediumThisField());
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
                                        setSrc("/#/amisSingle/index/" + m2mJoinButton.joinAnnoMainClazz().getSimpleName() + "?" + URLUtil.buildQuery(queryMap, null));
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
                                        setFailed("操作失败");
                                    }}
                                );

                            }}
                        );
                    } else {
                        continue;
                    }
                    // 添加对应按钮
                    Object buttons = columnJson.get("buttons");
                    if (buttons instanceof List<?> buttonList) {
                        List<Object> buttonListMap = (List<Object>) buttonList;
                        buttonListMap.add(action);
                        // 设置列宽
                        columnJson.put("width", buttonListMap.size() * 80);
                    }
                }
            }
        }
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
