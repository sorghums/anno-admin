package site.sorghum.anno.modular.amis.model;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.PrimaryKey;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.DrawerButton;
import site.sorghum.amis.entity.display.IFrame;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.function.ButtonGroup;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.input.InputTree;
import site.sorghum.amis.entity.input.TreeSelect;
import site.sorghum.amis.entity.layout.Page;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoLeftTree;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.modular.anno.annotation.field.AnnoButton;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import site.sorghum.anno.util.CryptoUtil;
import site.sorghum.anno.util.JSONUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树视图
 *
 * @author sorghum
 * @date 2023/07/02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeView extends Page {

    private TreeView(){

    }

    /**
     * 添加树表单
     *
     * @param clazz clazz
     */
    public void addTreeForm(Class<?> clazz) {
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
        Form crudForm = getCrudForm();
        crudForm.setBody(itemList);
        ButtonGroup crudButtonGroup = getCrudButtonGroup();
        List<Action> actions = crudButtonGroup.getButtons();
        Action action = actions.get(1);
        Map<String, Object> onEvent =
                action.getOnEvent();
        // 设置${_parentKey}的值
        String parentPk = AnnoUtil.getParentPk(clazz);
        JSONUtil.write(onEvent, "$.click.actions[1].args.value", new HashMap<>() {{
            put(parentPk, "${_cat}");
        }});
    }


    /**
     * 添加树边栏
     *
     * @param clazz      clazz
     * @param properties properties
     */
    public void addCommonTreeAside(Class<?> clazz, Map<String, Object> properties) {
        AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
        AnnoLeftTree annoLeftTree = annoMain.annoLeftTree();
        InputTree tree = new InputTree();
        tree.setId("aside-input-tree");
        tree.setName("_cat");
        tree.setSearchable(true);
        tree.setMultiple(false);
        tree.setCascade(false);
        tree.setHeightAuto(false);
        tree.setVirtualThreshold(9999);
        tree.setInputClassName("no-border no-padder mt-1");
        tree.setUnfoldedLevel(2);
        tree.setShowOutline(true);
        tree.setSource(new Api() {{
            setMethod("get");
            setUrl("/system/anno/${treeClazz}/annoTrees");
        }});
        Map<String, Object> event = new HashMap<>();
        event.put("change", new HashMap<String, Object>() {{
            put("actions", CollUtil.newArrayList(
                    new HashMap<String, Object>() {{
                        put("actionType", "broadcast");
                        put("args", new HashMap<String, Object>() {{
                            put("eventName", "broadcast_aside_change");
                        }});
                        put("data", new HashMap<String, Object>() {{
                            put("_cat", "${_cat}");
                        }});
                    }}
            ));
        }});
        tree.setOnEvent(event);
        if (annoLeftTree.enable()) {
            setAside(tree);
            return;
        }
        AnnoTree annoTree = annoMain.annoTree();
        if (annoTree.enable() && annoTree.displayAsTree()) {
            setAside(tree);
            return;
        }
    }

    /**
     * 添加Tree列按钮
     *
     * @param clazz clazz
     */
    public void addTreeColumnButtonInfo(Class<?> clazz) {
        List<Field> buttonFields = AnnoUtil.getAnnoButtonFields(clazz);
        // 读取现有的列
        Form formBody = getCrudForm();
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
                                        setSrc("/system/config/amis/" + o2MJoinButton.joinAnnoMainClazz().getSimpleName() + "?" + o2MJoinButton.joinOtherClazzField() + "=${" + o2MJoinButton.joinThisClazzField() + "}");
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
                                        setSrc("/system/config/amis-m2m/" + m2mJoinButton.joinAnnoMainClazz().getSimpleName() + "?" + URLUtil.buildQuery(queryMap, null));
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
            action.setHiddenOn("!_hasId");
            formBody.getActions().add(action);
        }
    }


    private Form getCrudForm(){
        return (Form) getBody().get(1);
    }

    private ButtonGroup getCrudButtonGroup(){
        return (ButtonGroup) getBody().get(0);
    }



    public static TreeView of(){
        return treeView();
    }
    private static TreeView treeView() {
        TreeView treeView = new TreeView();
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.setButtons(
                CollUtil.newArrayList(
                        new Action() {{
                            setLabel("新增");
                            setOnEvent(
                                    new HashMap<>() {{
                                        put("click",
                                                new HashMap<>(){{
                                                    put("actions",CollUtil.newArrayList(
                                                            new HashMap<String, Object>() {{
                                                                put("componentId", "tree-form-reload");
                                                                put("args", new HashMap<String, Object>());
                                                                put("actionType", "clear");
                                                            }},
                                                            new HashMap<String, Object>() {{
                                                                put("actionType","setValue");
                                                                put("componentId","tree-form-reload");
                                                                put("args",new HashMap<String,Object>(){{
                                                                    put("value",new HashMap<>(){{
                                                                        put("_hasId",false);
                                                                    }});
                                                                }});
                                                            }}
                                                    ));
                                                }}
                                        );
                                    }}
                            );
                            setBlock(false);
                            setLevel("info");
                        }},
                        new Action() {{
                            setLabel("新增下级");
                            setOnEvent(
                                    new HashMap<>() {{
                                        put("click",
                                                new HashMap<>(){{
                                                  put("actions",CollUtil.newArrayList(
                                                          new HashMap<String, Object>() {{
                                                              put("componentId", "tree-form-reload");
                                                              put("args", new HashMap<String, Object>());
                                                              put("actionType", "clear");
                                                          }},
                                                          new HashMap<String, Object>() {{
                                                              put("componentId", "tree-form-reload");
                                                              put("args", new HashMap<String, Object>() {{
                                                                  put("value", new HashMap<String, Object>() {{
                                                                      put("pid", "${_cat}");
                                                                  }});
                                                              }});
                                                              put("actionType", "setValue");
                                                          }},
                                                          new HashMap<String, Object>() {{
                                                              put("actionType","setValue");
                                                              put("componentId","tree-form-reload");
                                                              put("args",new HashMap<String,Object>(){{
                                                                  put("value",new HashMap<>(){{
                                                                        put("_hasId",false);
                                                                  }});
                                                              }});
                                                          }}
                                                  ));
                                                }}
                                        );
                                    }}
                            );
                            setLevel("light");
                        }}
                )
        );
        buttonGroup.setClassName("p-sm");
        buttonGroup.setVertical(false);
        Form form = new Form();
        form.setData(new HashMap<>() {{
            put("_hasId", false);
        }});
        form.setTitle("表单");
        form.setId("tree-form-reload");
        form.setMode("normal");
        form.setApi(new Api() {{
            setUrl("/system/anno/${clazz}/saveOrUpdate");
            setMethod("post");
            setMessages(new ApiMessage() {{
                setSuccess("保存成功");
                setFailed("保存失败");
            }});
        }});
        form.setInitApi(new Api() {{
            setUrl("/system/anno/${clazz}/queryById?_cat=${_cat}");
            setMethod("post");
        }});
        form.setReload("aside-input-tree, parent-tree-select");
        form.setActions(
                CollUtil.newArrayList(
                        new Action() {{
                            setType("submit");
                            setLabel("提交");
                            setId("tree-form-reload-submit");
                            setOnEvent(
                                    new HashMap<>() {{
                                        put("click",
                                                new HashMap<>(){{
                                                    put("actions",CollUtil.newArrayList(
                                                            new HashMap<String, Object>() {{
                                                                put("actionType","setValue");
                                                                put("componentId","tree-form-reload");
                                                                put("args",new HashMap<String,Object>(){{
                                                                    put("value",new HashMap<>(){{
                                                                        put("_hasId",true);
                                                                    }});
                                                                }});
                                                            }}
                                                    ));
                                                }}
                                        );
                                    }}
                            );
                            setLevel("primary");
                        }},
                        new Action() {{
                            setType("button");
                            setLabel("删除");
                            setId("tree-form-reload-delete");
                            setHiddenOn("!_hasId");
                            setOnEvent(
                                    new HashMap<>() {{
                                        put("click",
                                                new HashMap<>(){{
                                                    put("actions",CollUtil.newArrayList(
                                                            new HashMap<String, Object>() {{
                                                                put("args", new HashMap<String, Object>() {{
                                                                    put("options", new HashMap<String, Object>());
                                                                    put("api", new Api() {{
                                                                        setUrl("/system/anno/${clazz}/removeById?id=${id}");
                                                                        setMethod("post");
                                                                        setMessages(new ApiMessage() {{
                                                                            setSuccess("删除成功");
                                                                            setFailed("删除失败");
                                                                        }});
                                                                    }});
                                                                }});
                                                                put("outputVar", "responseResult");
                                                                put("actionType", "ajax");
                                                            }},
                                                            new HashMap<String, Object>() {{
                                                                put("componentId", "tree-form-reload");
                                                                put("args", new HashMap<String, Object>());
                                                                put("actionType", "clear");
                                                            }},
                                                            new HashMap<String, Object>() {{
                                                                put("componentId", "aside-input-tree");
                                                                put("actionType", "reload");
                                                            }},
                                                            new HashMap<String, Object>() {{
                                                                put("componentId", "parent-tree-select");
                                                                put("actionType", "reload");
                                                            }},
                                                            new HashMap<String, Object>() {{
                                                                put("actionType","setValue");
                                                                put("componentId","tree-form-reload");
                                                                put("args",new HashMap<String,Object>(){{
                                                                    put("value",new HashMap<>(){{
                                                                        put("_hasId",false);
                                                                    }});
                                                                }});
                                                            }}
                                                    ));
                                                }}
                                        );
                                    }}
                            );
                            setLevel("danger");
                        }}
                )
        );
        form.setOnEvent(
                new HashMap<>() {{
                    put("broadcast_aside_change",
                            new HashMap<>(){{
                                put("actions",CollUtil.newArrayList(
                                        new HashMap<String, Object>() {{
                                            put("componentId", "tree-form-reload");
                                            put("actionType", "clear");
                                        }},
                                        new HashMap<String, Object>() {{
                                            put("componentId", "tree-form-reload");
                                            put("actionType", "reload");
                                        }},
                                        new HashMap<String, Object>() {{
                                            put("actionType","setValue");
                                            put("componentId","tree-form-reload");
                                            put("args",new HashMap<String,Object>(){{
                                                put("value",new HashMap<>(){{
                                                    put("_hasId",true);
                                                }});
                                            }});
                                        }}
                                ));
                            }}
                    );
                }}
        );
        treeView.setBody(CollUtil.newArrayList(buttonGroup, form));
        treeView.setAsideResizor(true);
        treeView.setAsideMinWidth(220);
        treeView.setAsideMaxWidth(350);
        return treeView;
    }
}
