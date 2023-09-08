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
 * DAG 工作流信息表
 *
 * @author tjq
 * @since 2020/5/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "DAG 工作流信息表")
@NoArgsConstructor
@AllArgsConstructor
@Table("pj_workflow_info")
public class WorkflowInfoDO extends BaseMetaModel {

    @AnnoField(title = "工作流名称")
    private String wfName;

    @AnnoField(title = "工作流描述")
    private String wfDescription;

    /**
     * 所属应用ID
     */
    @AnnoField(title = "所属应用ID")
    private String appId;

    /**
     * 工作流的DAG图信息（点线式DAG的json）
     */
    @AnnoField(title = "DAG图信息", dataType = AnnoDataType.CODE_EDITOR)
    private String peDAG;

    /* ************************** 定时参数 ************************** */
    /**
     * 时间表达式类型（CRON/API/FIX_RATE/FIX_DELAY）
     */
    @AnnoField(title = "时间表达式类型")
    private Integer timeExpressionType;
    /**
     * 时间表达式，CRON/NULL/LONG/LONG
     */
    @AnnoField(title = "时间表达式")
    private String timeExpression;

    /**
     * 最大同时运行的工作流个数，默认 1
     */
    @AnnoField(title = "最大同时运行的工作流个数")
    private Integer maxWfInstanceNum;

    /**
     * 1 正常运行，2 停止（不再调度）
     */
    @AnnoField(title = "状态")
    private Integer status;
    /**
     * 下一次调度时间
     */
    @AnnoField(title = "下一次调度时间")
    private Long nextTriggerTime;
    /**
     * 工作流整体失败的报警
     */
    @AnnoField(title = "工作流整体失败的报警")
    private String notifyUserIds;

    /**
     * 任务生命周期（开始和结束时间）
     */
    @AnnoField(title = "任务生命周期")
    private String lifecycle;

    @AnnoField(title = "附加参数", dataType = AnnoDataType.CODE_EDITOR)
    private String extra;
}
