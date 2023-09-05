package tech.powerjob.server.solon.persistence.remote.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.pre.suppose.model.BaseMetaModel;
import tech.powerjob.common.enums.InstanceStatus;

/**
 * 任务运行日志表
 *
 * @author tjq
 * @since 2020/3/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "任务运行日志表")
@NoArgsConstructor
@AllArgsConstructor
@Table("pj_instance_info")
public class InstanceInfoDO extends BaseMetaModel {

    /**
     * 任务ID
     */
    @AnnoField(title = "任务ID")
    private String jobId;
    /**
     * 任务所属应用的ID，冗余提高查询效率
     */
    @AnnoField(title = "任务所属应用的ID，冗余提高查询效率")
    private String appId;
    /**
     * TODO 任务实例ID，是否可以删掉？
     */
    @AnnoField(title = "任务实例ID")
    private String instanceId;
    /**
     * 任务参数（静态）
     *
     * @since 2021/2/01
     */
    @AnnoField(title = "任务参数（静态）", dataType = AnnoDataType.CODE_EDITOR)
    private String jobParams;
    /**
     * 任务实例参数（动态）
     */
    @AnnoField(title = "任务实例参数（动态）", dataType = AnnoDataType.CODE_EDITOR)
    private String instanceParams;
    /**
     * 该任务实例的类型，普通/工作流（InstanceType）
     */
    @AnnoField(title = "类型：普通/工作流")
    private Integer type;
    /**
     * 该任务实例所属的 workflow ID，仅 workflow 任务存在
     */
    @AnnoField(title = "工作流id")
    private String wfInstanceId;
    /**
     * 任务状态 {@link InstanceStatus}
     */
    @AnnoField(title = "任务状态")
    private Integer status;
    /**
     * 执行结果（允许存储稍大的结果）
     */
    @AnnoField(title = "执行结果", dataType = AnnoDataType.CODE_EDITOR)
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
    /**
     * 最后上报时间
     */
    @AnnoField(title = "最后上报时间")
    private Long lastReportTime;
    /**
     * TaskTracker 地址
     */
    @AnnoField(title = "TaskTracker地址")
    private String taskTrackerAddress;
    /**
     * 总共执行的次数（用于重试判断）
     */
    @AnnoField(title = "总共执行次数")
    private Long runningTimes;

}
