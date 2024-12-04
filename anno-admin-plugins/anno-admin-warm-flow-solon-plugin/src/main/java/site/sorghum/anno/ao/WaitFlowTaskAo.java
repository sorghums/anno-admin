package site.sorghum.anno.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.warm.flow.core.entity.User;
import org.dromara.warm.flow.orm.entity.FlowTask;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.proxy.field.SnowIdLongSupplier;
import site.sorghum.anno.anno.proxy.field.ZeroFiledStringBaseSupplier;
import site.sorghum.anno.cmd.DoneHisTransactCmd;
import site.sorghum.anno.cmd.GetFlowImgCmd;
import site.sorghum.anno.enums.NodeTypeEnum;
import site.sorghum.anno.enums.TrueFalseCharEnum;
import site.sorghum.anno.form.TransactForm;
import site.sorghum.plugin.join.aop.JoinResMap;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AnnoMain(name = "待办任务",
        annoPermission = @AnnoPermission(baseCode = "wait_flow_task", baseCodeTranslate = "待办任务"),
        autoMaintainTable = false,
        virtualTable = true
)
@EqualsAndHashCode(callSuper = true)
@Data
public class WaitFlowTaskAo extends FlowTask {

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
    @AnnoField(title = "流程实例",
            tableFieldName = "instance_id")
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
    public String getFlowName() {
        return super.getFlowName();
    }

    @Override
    public List<String> getPermissionList() {
        return super.getPermissionList();
    }

    @Override
    public List<User> getUserList() {
        return super.getUserList();
    }

    @Override
    @AnnoField(title = "审批表单自定义", tableFieldName = "form_custom",
            dataType = AnnoDataType.RADIO,
            optionType = @AnnoOptionType(optionEnum = TrueFalseCharEnum.class), edit = @AnnoEdit(span = 24))
    public String getFormCustom() {
        return super.getFormCustom();
    }

    @Override
    @AnnoField(title = "审批表单路径", tableFieldName = "form_path", edit = @AnnoEdit(span = 24))
    public String getFormPath() {
        return super.getFormPath();
    }

    @AnnoButton(name = "办理",
            baseForm = TransactForm.class,
            javaCmd = @AnnoButton.JavaCmd(
                    runSupplier = DoneHisTransactCmd.class
            )
    )
    Object transactMethod;

    @AnnoButton(name = "流程图",
            javaCmd = @AnnoButton.JavaCmd(runSupplier = GetFlowImgCmd.class))
    Object flowImageMethod;

    @JoinResMap
    Map<String, Object> joinResMap = new HashMap<>();
}
