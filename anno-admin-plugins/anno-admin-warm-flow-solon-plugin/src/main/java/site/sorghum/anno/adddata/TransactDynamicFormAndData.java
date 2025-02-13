package site.sorghum.anno.adddata;

import org.dromara.warm.flow.core.entity.Node;
import org.dromara.warm.flow.core.service.NodeService;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno.anno.annotation.field.AnnoEditImpl;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionTypeImpl;
import site.sorghum.anno.anno.datasupplier.AnnoDynamicFormAndDataSupplier;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.ao.WaitFlowTaskAo;
import site.sorghum.anno.form.TransactForm;
import site.sorghum.anno.service.AnnoFlowService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class TransactDynamicFormAndData implements AnnoDynamicFormAndDataSupplier {

    @Inject
    AnnoFlowService annoFlowService;

    @Inject
    NodeService nodeService;

    public TransactDynamicFormAndData() {
        AnnoDynamicFormAndDataSupplier.putInstance(TransactForm.class, this);
    }

    @Override
    public Map<String, Object> get(String entityName, List<String> columnDataIds) {
        return Map.of(
            "status", "1",
            "message", "暂无"
        );
    }

    @Override
    public List<AnField> getExtraDynamicInput(String entityName, List<String> columnDataIds) {
        String id = columnDataIds.get(0);
        WaitFlowTaskAo waitFlowTaskAo = annoFlowService.toDoOne(id);
        List<Node> nextPassNodeList = Collections.emptyList();
        try {
            nextPassNodeList = nodeService.getNextNodeList(waitFlowTaskAo.getDefinitionId(), waitFlowTaskAo.getNodeCode(), "", "PASS", null);
        }catch (Exception ignore){}
        List<Node> nextRejectNodeList = Collections.emptyList();
        try {
            nextRejectNodeList = nodeService.getNextNodeList(waitFlowTaskAo.getDefinitionId(), waitFlowTaskAo.getNodeCode(), "", "REJECT", null);
        } catch (Exception ignore){}
        AnField nextPassCodeField = new AnField();
        nextPassCodeField.setJavaName("nextPassNodeCode");
        nextPassCodeField.setDataType(AnnoDataType.OPTIONS);
        nextPassCodeField.setOptionType(
            AnnoOptionTypeImpl
                .builder()
                .value(
                    nextPassNodeList.stream().map(node -> {
                        AnnoOptionTypeImpl.OptionDataImpl optionData = new AnnoOptionTypeImpl.OptionDataImpl();
                        optionData.setLabel(node.getNodeName());
                        optionData.setValue(node.getNodeCode());
                        return optionData;
                    }).toArray(AnnoOptionTypeImpl.OptionDataImpl[]::new)
                )
                .build()
        );
        nextPassCodeField.setTitle("通过节点");
        nextPassCodeField.setTableFieldName("nextPassNodeId");
        nextPassCodeField.setEdit(AnnoEditImpl
            .builder()
            .addEnable(true)
                .showBy(AnnoEditImpl
                    .ShowByImpl
                    .builder()
                    .enable(true)
                    .expr("annoDataForm.status == 1")
                    .build()
                )
            .build());

        AnField nextRejectCodeField = new AnField();
        nextRejectCodeField.setJavaName("nextRejectNodeCode");
        nextRejectCodeField.setDataType(AnnoDataType.OPTIONS);
        nextRejectCodeField.setOptionType(
            AnnoOptionTypeImpl
                .builder()
                .value(
                    nextRejectNodeList.stream().map(node -> {
                        AnnoOptionTypeImpl.OptionDataImpl optionData = new AnnoOptionTypeImpl.OptionDataImpl();
                        optionData.setLabel(node.getNodeName());
                        optionData.setValue(node.getNodeCode());
                        return optionData;
                    }).toArray(AnnoOptionTypeImpl.OptionDataImpl[]::new)
                )
                .build()
        );
        nextRejectCodeField.setTitle("驳回节点");
        nextRejectCodeField.setTableFieldName("nextRejectNodeId");
        nextRejectCodeField.setEdit(AnnoEditImpl
            .builder()
            .addEnable(true)
            .showBy(AnnoEditImpl
                .ShowByImpl
                .builder()
                .enable(true)
                .expr("annoDataForm.status == 2")
                .build()
            )
            .build());
        return List.of(
            nextPassCodeField,
            nextRejectCodeField
        );
    }
}
