package site.sorghum.anno.amis.model;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.layout.Page;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * crud视图
 *
 * @author sorghum
 * @since 2023/07/02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CrudView extends Page {
    
    @Transient
    public Crud getCrudBody(){
        return (Crud) this.getBody().get(0);
    }

    public static CrudView of(){
        return crudBasePage();
    }

    private static CrudView crudBasePage(){
        CrudView page = new CrudView();
        page.setId("crud_base_page");
        page.setAsideResizor(true);
        Crud bodyCrud = new Crud();
        bodyCrud.setId("crud_template_main");
        bodyCrud.setDraggable(false);
        bodyCrud.setPerPage(10);
        bodyCrud.setSyncLocation(false);
        bodyCrud.setApi(
                new Api(){{
                    setMethod("post");
                    setUrl("/amis/system/anno/${clazz}/page");
                    setData(new HashMap<>(){{
                        put("&", "$$");
                        put("_cat", "${_cat}");
                        put("ignoreM2m", false);
                        put("reverseM2m", false);
                        put("_extraData", "${extraData}");
                    }});
                }}
        );
        bodyCrud.setHeaderToolbar(CollUtil.newArrayList("export-excel", "bulkActions"));
        bodyCrud.setFooterToolbar(CollUtil.newArrayList("statistics", "switch-per-page", "pagination"));
        bodyCrud.setColumns(
                new ArrayList<>(){{
                    add(
                            new HashMap<String,Object>(){{
                                put("type","operation");
                                put("label","操作");
                                put("buttons",new ArrayList<>());
                                put("fixed","right");
                            }}
                    );
                }}
        );
        page.setBody(CollUtil.newArrayList(bodyCrud));
        page.setAsideMaxWidth(350);
        return page;
    }

}
