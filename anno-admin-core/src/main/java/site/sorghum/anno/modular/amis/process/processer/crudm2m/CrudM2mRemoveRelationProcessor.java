package site.sorghum.anno.modular.amis.process.processer.crudm2m;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson2.JSONObject;
import org.noear.solon.annotation.Component;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.anno.modular.amis.model.CrudM2mView;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;

import java.util.List;
import java.util.Map;

/**
 * crud-m2m删除关系处理器
 *
 * @author Sorghum
 * @since 2023/07/10
 */
@Component
public class CrudM2mRemoveRelationProcessor implements BaseProcessor {
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        CrudM2mView crudM2mView = (CrudM2mView) amisBaseWrapper.getAmisBase();
        // 删除按钮模板
        Action delete = new Action();
        delete.setType("button");
        delete.setActionType("ajax");
        delete.setLevel("danger");
        delete.setLabel("删除关联关系");
        delete.setConfirmText("您确认要删除关联关系吗?");
        delete.setApi(
                new Api() {{
                    setMethod("post");
                    setUrl("/system/anno/${clazz}/remove-relation");
                    setData(new JSONObject() {{
                        put("&", "$$");
                        put("_extraData", "${extraData}");
                    }});
                    setMessages(
                            new ApiMessage() {{
                                setSuccess("操作成功");
                                setFailed("操作失败");
                            }}
                    );
                }}
        );
        Crud crudBody = crudM2mView.getCrudBody();
        // 读取现有的列
        List<Map> columns = crudBody.getColumns();
        for (Map columnJson : columns) {
            if ("操作".equals(MapUtil.getStr(columnJson, "label"))) {
                // 添加删除按钮
                Object buttons = columnJson.get("buttons");
                if (buttons instanceof List<?> buttonList) {
                    List<Object> buttonListCommon = (List<Object>) buttonList;
                    // 设置列宽
                    buttonListCommon.add(delete);
                    columnJson.put("width", buttonListCommon.size() * 80);
                }
            }
        }
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
