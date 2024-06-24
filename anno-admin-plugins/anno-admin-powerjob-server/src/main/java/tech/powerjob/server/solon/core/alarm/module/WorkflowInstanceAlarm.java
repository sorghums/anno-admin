package tech.powerjob.server.solon.core.alarm.module;

import lombok.Data;
import tech.powerjob.common.model.PEWorkflowDAG;
import tech.powerjob.server.solon.extension.alarm.Alarm;

/**
 * 工作流执行失败告警对象
 *
 * @author tjq
 * @since 2020/6/12
 */
@Data
public class WorkflowInstanceAlarm implements Alarm {

    private String workflowName;

    /**
     * 任务所属应用的ID，冗余提高查询效率
     */
    private String appId;
    private String workflowId;
    /**
     * workflowInstanceId（任务实例表都使用单独的ID作为主键以支持潜在的分表需求）
     */
    private String wfInstanceId;
    /**
     * workflow 状态（WorkflowInstanceStatus）
     */
    private Integer status;

    private PEWorkflowDAG peWorkflowDAG;
    private String result;

    /**
     * 实际触发时间
     */
    private Long actualTriggerTime;
    /**
     * 结束时间
     */
    private Long finishedTime;

    /**
     * 时间表达式类型（CRON/API/FIX_RATE/FIX_DELAY）
     */
    private Integer timeExpressionType;
    /**
     * 时间表达式，CRON/NULL/LONG/LONG
     */
    private String timeExpression;

    @Override
    public String fetchTitle() {
        return "PowerJob AlarmService: Workflow Running Failed";
    }
}
