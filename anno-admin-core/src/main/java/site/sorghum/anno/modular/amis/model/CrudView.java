package site.sorghum.anno.modular.amis.model;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.PrimaryKey;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.display.*;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.Form;
import site.sorghum.amis.entity.input.FormItem;
import site.sorghum.amis.entity.input.InputTree;
import site.sorghum.amis.entity.layout.Page;
import site.sorghum.anno.exception.BizException;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoLeftTree;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.modular.anno.annotation.field.AnnoButton;
import site.sorghum.anno.modular.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.anno.util.AnnoUtil;
import site.sorghum.anno.util.CryptoUtil;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



    //----------------------静态方法初始化---------------------- 
    
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
                    setUrl("/system/anno/${clazz}/page");
                    setData(new HashMap<>(){{
                        put("&", "$$");
                        put("_cat", "${_cat}");
                        put("ignoreM2m", false);
                        put("reverseM2m", false);
                        put("_extraData", "${extraData}");
                    }});
                }}
        );
        bodyCrud.setHeaderToolbar(CollUtil.newArrayList("export-excel", "bulkActions", ""));
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
