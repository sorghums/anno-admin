package tech.powerjob.server.solon.persistence.remote.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.suppose.model.BaseMetaModel;

/**
 * 工作流运行实例表
 *
 * @author tjq
 * @since 2020/5/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "工作流运行实例表")
@NoArgsConstructor
@AllArgsConstructor
@Table("pj_workflow_instance_info")
public class WorkflowInstanceInfoDO extends BaseMetaModel {

    /**
     * 任务所属应用的ID，冗余提高查询效率
     */
    @AnnoField(title = "任务所属应用的ID")
    private String appId;
    /**
     * workflowInstanceId（任务实例表都使用单独的ID作为主键以支持潜在的分表需求）
     */
    @AnnoField(title = "工作流实例id")
    private String wfInstanceId;
    /**
     * 上层工作流实例 ID （用于支持工作流嵌套）
     */
    @AnnoField(title = "父级工作流实例id")
    private String parentWfInstanceId;

    @AnnoField(title = "工作流id")
    private String workflowId;
    /**
     * workflow 状态（WorkflowInstanceStatus）
     */
    @AnnoField(title = "工作流状态")
    private Integer status;
    /**
     * 工作流启动参数
     */
    @AnnoField(title = "工作流启动参数", dataType = AnnoDataType.CODE_EDITOR)
    private String wfInitParams;
    /**
     * 工作流上下文
     */
    @AnnoField(title = "工作流上下文", dataType = AnnoDataType.CODE_EDITOR)
    private String wfContext;

    @AnnoField(title = "dag", dataType = AnnoDataType.CODE_EDITOR)
    private String dag;

    /**
     * 工作流实例的结果
     */
    @AnnoField(title = "工作流实例的结果", dataType = AnnoDataType.CODE_EDITOR)
    private String result;
    /**
     * 预计触发时间
     */
    @AnnoField(title = "预计触发时间")
    private Long expectedTriggerTime;
    /**
     * 实际触发时间
     */
    @AnnoField(title = "实际触发时间")
    private Long actualTriggerTime;
    /**
     * 结束时间
     */
    @AnnoField(title = "结束时间")
    private Long finishedTime;

}
