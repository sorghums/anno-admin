package site.sorghum.anno.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.warm.flow.orm.entity.FlowDefinition;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.enums.TrueFalseEnum;
import site.sorghum.anno.anno.proxy.field.SnowIdLongSupplier;
import site.sorghum.anno.anno.proxy.field.ZeroFiledStringBaseSupplier;
import site.sorghum.anno.cmd.FlowDefinitionPublishCmd;
import site.sorghum.anno.cmd.FlowDefinitionUnPublishCmd;
import site.sorghum.anno.enums.TrueFalseCharEnum;
import site.sorghum.plugin.join.aop.JoinResMap;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@AnnoMain(name = "流程定义",
    tableName = "flow_definition",
    annoPermission = @AnnoPermission(baseCode = "flow_definition", baseCodeTranslate = "流程定义"),
    autoMaintainTable = false
)
@EqualsAndHashCode(callSuper = true)
@Data
public class FlowDefinitionAo extends FlowDefinition {
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
    @AnnoField(title = "流程编码", tableFieldName = "flow_code", edit = @AnnoEdit(span = 24), sort = 1000)
    public String getFlowCode() {
        return super.getFlowCode();
    }

    @Override
    @AnnoField(title = "流程名称", tableFieldName = "flow_name", edit = @AnnoEdit(span = 24), sort = 900)
    public String getFlowName() {
        return super.getFlowName();
    }

    @Override
    @AnnoField(title = "流程版本", tableFieldName = "version", edit = @AnnoEdit(span = 24), sort = 800)
    public String getVersion() {
        return super.getVersion();
    }

    @Override
    @AnnoField(title = "是否发布", tableFieldName = "is_publish",
        dataType = AnnoDataType.RADIO,
        optionType = @AnnoOptionType(optionEnum = TrueFalseEnum.class), edit = @AnnoEdit(span = 24), sort = 700)
    public Integer getIsPublish() {
        return super.getIsPublish();
    }

    @Override
    @AnnoField(title = "审批表单自定义", tableFieldName = "form_custom",
        dataType = AnnoDataType.RADIO,
        optionType = @AnnoOptionType(optionEnum = TrueFalseCharEnum.class), edit = @AnnoEdit(span = 24), sort = 600)
    public String getFormCustom() {
        return super.getFormCustom();
    }

    @Override
    @AnnoField(title = "审批表单路径", tableFieldName = "form_path", edit = @AnnoEdit(span = 24), sort = 500)
    public String getFormPath() {
        return super.getFormPath();
    }


    @AnnoButton(
        name = "流程设计",
        permissionCode = "flow_design",
        icon = "ant-design:delivered-procedure-outlined"
    )
    Object flowDesign;

    @AnnoButton(
        name = "流程导出",
        permissionCode = "export_flow",
        icon = "ant-design:export-outlined"
    )
    Object exportFlow;

    @AnnoButton(
        name = "发布",
        permissionCode = "publish",
        javaCmd = @AnnoButton.JavaCmd(runSupplier = FlowDefinitionPublishCmd.class),
        icon = "ant-design:arrow-up-outlined"
    )
    Object publish;

    @AnnoButton(name = "取消发布",
        permissionCode = "cancel_publish",
        javaCmd = @AnnoButton.JavaCmd(runSupplier = FlowDefinitionUnPublishCmd.class),
        icon = "ant-design:arrow-down-outlined")
    Object cancelPublish;

    @JoinResMap
    Map<String, Object> joinResMap = new HashMap<>();
}
