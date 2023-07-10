package site.sorghum.anno.modular.amis.model;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.display.*;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.layout.Page;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Crud多对多视图
 *
 * @author sorghum
 * @since 2023/07/02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CrudM2mView extends Page {

    @Transient
    public Crud getCrudBody() {
        return (Crud) getBody().get(0);
    }

    public static CrudM2mView of() {
        return crudM2mView();
    }

    private static CrudM2mView crudM2mView() {
        CrudM2mView crudM2mView = new CrudM2mView();
        crudM2mView.setAsideResizor(true);
        crudM2mView.setAsideMaxWidth(350);
        Crud bodyCrud = new Crud();
        bodyCrud.setDraggable(false);
        bodyCrud.setPerPage(10);
        bodyCrud.setId("m2m-crud");
        bodyCrud.setSyncLocation(false);
        bodyCrud.setApi(
                new Api() {{
                    setMethod("post");
                    setUrl("/system/anno/${clazz}/page");
                    setData(
                            new HashMap<>() {{
                                put("_cat", "${_cat}");
                                put("_extraData", "${extraData}");
                                put("&", "$$");
                            }}
                    );
                }}
        );
        bodyCrud.setOnEvent(
                new HashMap<>() {{
                    put("broadcast_refresh_m2m_crud", new HashMap<>() {{
                        put("actions", CollUtil.newArrayList(
                                new HashMap<>() {{
                                    put("actionType", "reload");
                                    put("componentId", "m2m-crud");
                                }}
                        ));
                    }});
                }}
        );
        bodyCrud.setKeepItemSelectionOnPageChange(true);
        bodyCrud.setHeaderToolbar(
                CollUtil.newArrayList(
                        "bulkActions",
                        "reload",
                        new DrawerButton() {{
                            setLabel("新增关联关系");
                            setLevel("primary");
                            setDrawer(
                                    new DrawerButton.Drawer() {{
                                        setSize("xl");
                                        setPosition("right");
                                        setHeaderClassName("p-none m-none h-0");
                                        setActions(CollUtil.newArrayList());
                                        setShowCloseButton(false);
                                        setCloseOnOutside(true);
                                        setOnEvent(
                                                new HashMap<>() {{
                                                    put("confirm", new HashMap<>() {{
                                                        put("actions", CollUtil.newArrayList(
                                                                new HashMap<>() {{
                                                                    put("actionType", "broadcast");
                                                                    put("args", new HashMap<>() {{
                                                                        put("eventName", "broadcast_refresh_m2m_crud");
                                                                    }});
                                                                    put("data", new HashMap<>() {{
                                                                    }});
                                                                }}
                                                        ));
                                                    }});
                                                }}
                                        );
                                    }}
                            );
                        }}
                )
        );
        bodyCrud.setFooterToolbar(
                CollUtil.newArrayList(
                        "statistics",
                        "switch-per-page",
                        "pagination"

                )
        );
        bodyCrud.setColumns(
                CollUtil.newArrayList(
                        new HashMap<>() {{
                            put("type", "operation");
                            put("label", "操作");
                            put("buttons", new ArrayList<>());
                            put("fixed", 100);
                        }}
                )
        );
        crudM2mView.setBody(CollUtil.newArrayList(bodyCrud));
        return crudM2mView;
    }
}
