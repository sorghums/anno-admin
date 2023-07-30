package site.sorghum.anno.modular.amis.process.processer.crud;

import cn.hutool.core.map.MapUtil;
import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.anno.modular.amis.model.CrudView;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;

import java.util.List;
import java.util.Map;

/**
 * CRUD视图初始化处理器
 *
 * @author Sorghum
 * @since 2023/07/07
 */
@Named
public class CrudDeleteBtnProcessor implements BaseProcessor {
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain){
        CrudView crudView = (CrudView) amisBaseWrapper.getAmisBase();
        // 删除按钮模板
        Action delete = new Action();
        delete.setActionType("ajax");
        delete.setLabel("删除");
        delete.setLevel("danger");
        delete.setConfirmText("您确认要删除?");
        delete.setApi(new Api() {{
            setMethod("post");
            setUrl("/system/anno/${clazz}/removeById");
            setMessages(
                    new ApiMessage() {{
                        setSuccess("删除成功");
                        setFailed("删除失败");
                    }}
            );
        }});
        // 读取现有的列
        Crud crudBody = crudView.getCrudBody();
        List<Map> columns = crudBody.getColumns();
        for (Map columnMap : columns) {
            if ("操作".equals(MapUtil.getStr(columnMap, "label"))) {
                // 添加删除按钮
                Object buttons = columnMap.get("buttons");
                if (buttons instanceof List<?> buttonList) {
                    List<Object> buttonListMap = (List<Object>) buttonList;
                    buttonListMap.add(delete);
                }
                break;
            }
        }
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
