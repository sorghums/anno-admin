package site.sorghum.anno.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.warm.flow.orm.entity.FlowHisTask;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.proxy.field.SnowIdLongSupplier;
import site.sorghum.anno.anno.proxy.field.ZeroFiledStringBaseSupplier;
import site.sorghum.anno.enums.FlowStatusEnum;
import site.sorghum.anno.enums.NodeTypeEnum;
import site.sorghum.plugin.join.aop.JoinResMap;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@AnnoMain(name = "历史任务记录",
    tableName = "flow_his_task",
    annoPermission = @AnnoPermission(baseCode = "flow_his_task", baseCodeTranslate = "历史任务记录"),
    autoMaintainTable = false
)
@EqualsAndHashCode(callSuper = true)
@Data
public class FlowHisTaskAo extends FlowHisTask {

    @Override
    @AnnoField(title = "主键", tableFieldName = "id", show = false, fieldSize = 32, insertWhenNullSet = SnowIdLongSupplier.class, pkField = true)
    public Long getId() {
        return super.getId();
    }

    @Override
    @AnnoField(title = "创建时间", tableFieldName = "create_time", dataType = AnnoDataType.DATETIME, show = false)
    public Date getCreateTime() {
        return super.getCreateTime();
    }

    @Override
    @AnnoField(title = "更新时间", tableFieldName = "update_time", dataType = AnnoDataType.DATETIME, show = false)
    public Date getUpdateTime() {
        return super.getUpdateTime();
    }

    @Override
    public String getTenantId() {
        return super.getTenantId();
    }

    @Override
    @AnnoField(title = "删除标识", tableFieldName = "del_flag",
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "已删除", value = "1"),
            @AnnoOptionType.OptionData(label = "正常", value = "0")
        }), show = false, fieldSize = 1, insertWhenNullSet = ZeroFiledStringBaseSupplier.class)
    public String getDelFlag() {
        return super.getDelFlag();
    }

    @Override
    @AnnoField(title = "流程定义", tableFieldName = "definition_id",
        dataType = AnnoDataType.CLASS_OPTIONS,
        optionType = @AnnoOptionType(optionAnno = @AnnoOptionType.OptionAnnoClass(annoClass = FlowDefinitionAo.class, labelKey = "flowName")),
        edit = @AnnoEdit)
    public Long getDefinitionId() {
        return super.getDefinitionId();
    }


    @Override
    @AnnoField(title = "流程实例", tableFieldName = "instance_id", edit = @AnnoEdit)
    public Long getInstanceId() {
        return super.getInstanceId();
    }

    @Override
    @AnnoField(title = "开始节点编码", tableFieldName = "node_code", edit = @AnnoEdit)
    public String getNodeCode() {
        return super.getNodeCode();
    }

    @Override
    @AnnoField(title = "开始节点名称", tableFieldName = "node_name", edit = @AnnoEdit)
    public String getNodeName() {
        return super.getNodeName();
    }

    @Override
    @AnnoField(title = "开始节点类型", tableFieldName = "node_type",
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(optionEnum = NodeTypeEnum.class), edit = @AnnoEdit)
    public Integer getNodeType() {
        return super.getNodeType();
    }

    @Override
    @AnnoField(title = "目标节点编码", tableFieldName = "target_node_code", edit = @AnnoEdit)
    public String getTargetNodeCode() {
        return super.getTargetNodeCode();
    }

    @Override
    @AnnoField(title = "目标节点名称", tableFieldName = "target_node_name", edit = @AnnoEdit)
    public String getTargetNodeName() {
        return super.getTargetNodeName();
    }

    @Override
    @AnnoField(title = "审批者", tableFieldName = "approver", edit = @AnnoEdit)
    public String getApprover() {
        return super.getApprover();
    }

    @Override
    @AnnoField(title = "流程状态", tableFieldName = "flow_status",
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(optionEnum = FlowStatusEnum.class), edit = @AnnoEdit)
    public String getFlowStatus() {
        return super.getFlowStatus();
    }

    @Override
    @AnnoField(title = "审批意见", tableFieldName = "message", edit = @AnnoEdit)
    public String getMessage() {
        return super.getMessage();
    }


    @JoinResMap
    Map<String, Object> joinResMap = new HashMap<>();
}
