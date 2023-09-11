package site.sorghum.anno.amis.process.processer.crud;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.function.Action;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.amis.model.CrudView;
import site.sorghum.anno.amis.process.BaseProcessor;
import site.sorghum.anno.amis.process.BaseProcessorChain;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * CRUD视图初始化处理器
 *
 * @author Sorghum
 * @since 2023/07/07
 */
@Named
public class CrudDeleteBtnProcessor implements BaseProcessor {

    @Inject
    MetadataManager metadataManager;

    private static final String DELETE_BUTTON_LABEL = "删除";

    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain){
        CrudView crudView = (CrudView) amisBaseWrapper.getAmisBase();
        AnEntity entity = metadataManager.getEntity(clazz);
        if (!entity.isCanRemove()) {
            chain.doProcessor(amisBaseWrapper, clazz, properties);
            return;
        }
        // 删除按钮模板
        Action delete = createDeleteAction(clazz);
        // 读取现有的列
        Crud crudBody = crudView.getCrudBody();
        List<Map> columns = crudBody.getColumns();
        Optional<Map> operationColumn = columns.stream()
            .filter(columnMap -> "操作".equals(columnMap.get("label")))
            .findFirst();
        operationColumn.ifPresent(columnMap -> {
            // 添加删除按钮
            Object buttons = columnMap.get("buttons");
            if (buttons instanceof List<?>) {
                List<Object> buttonListMap = (List<Object>) buttons;
                buttonListMap.add(delete);
            }
        });
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }

    private Action createDeleteAction(Class<?> clazz) {
        Action delete = new Action();
        delete.setActionType("ajax");
        delete.setLabel(DELETE_BUTTON_LABEL);
        delete.setLevel("danger");
        delete.setConfirmText("您确认要删除?");
        delete.setApi(createDeleteApi(clazz));
        return delete;
    }

    private Api createDeleteApi(Class<?> clazz) {
        Api api = new Api();
        api.setMethod("post");
        api.setUrl("/amis/system/anno/" + clazz.getSimpleName() + "/removeById");
        return api;
    }
}
