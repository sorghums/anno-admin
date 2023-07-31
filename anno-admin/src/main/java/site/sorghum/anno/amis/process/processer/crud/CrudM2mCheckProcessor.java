package site.sorghum.anno.amis.process.processer.crud;

import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.anno.amis.model.CrudView;
import site.sorghum.anno.amis.process.BaseProcessor;
import site.sorghum.anno.amis.process.BaseProcessorChain;

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
public class CrudM2mCheckProcessor implements BaseProcessor {
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain){
        if (properties.getOrDefault("isM2m", false).equals(false)){
            chain.doProcessor(amisBaseWrapper, clazz, properties);
            return;
        }
        CrudView page = ((CrudView) amisBaseWrapper.getAmisBase());
        Crud crudBody = page.getCrudBody();
        Api api = crudBody.getApi();
        Map<String, Object> data = api.getData();
        if (data == null) {
            data = new HashMap<>();
            api.setData(data);
        }
        data.put("reverseM2m", true);
        List<Action> bulkActions = crudBody.getBulkActions();
        if (bulkActions == null) {
            bulkActions = new ArrayList<>();
            crudBody.setBulkActions(bulkActions);
        }
        Action insertRelations = new Action();
        insertRelations.setLabel("批量新增关系");
        insertRelations.setActionType("ajax");
        insertRelations.setLevel("primary");
        insertRelations.setApi(new Api(){{
            setMethod("post");
            setUrl("/system/anno/${clazz}/addM2m");
            setData(new HashMap<String, Object>(){{
                put("&", "$$");
                put("_extraData", "${extraData}");
            }});
            setMessages(new ApiMessage(){{
                setSuccess("操作成功");
                setFailed("操作失败");
            }});
        }});
        insertRelations.setReload("m2m-crud,crud_template_main");
        bulkActions.add(insertRelations);
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
