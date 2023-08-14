package site.sorghum.anno.amis.model;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.function.ButtonGroup;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.layout.Page;

import java.beans.Transient;
import java.util.HashMap;

/**
 * 树视图
 *
 * @author sorghum
 * @since 2023/07/02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeView extends Page {

    @Transient
    public Form getCrudForm(){
        return (Form) getBody().get(1);
    }

    @Transient
    public ButtonGroup getCrudButtonGroup(){
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
        form.setColumnCount(2);
        form.setTitle("表单");
        form.setId("tree-form-reload");
        form.setMode("normal");
        form.setApi(new Api() {{
            setUrl("/amis/system/anno/${clazz}/saveOrUpdate");
            setMethod("post");
            setMessages(new ApiMessage() {{
                setSuccess("保存成功");
                setFailed("保存失败");
            }});
        }});
        form.setInitApi(new Api() {{
            setUrl("/amis/system/anno/${clazz}/queryById?_cat=${_cat}");
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
                                                                        setUrl("/amis/system/anno/${clazz}/removeById?id=${id}");
                                                                        setMethod("post");
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
        treeView.setAsideMaxWidth(350);
        return treeView;
    }
}
