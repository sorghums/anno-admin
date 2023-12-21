package tech.powerjob.server.solon.persistence.remote.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoOrder;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.common.AnnoTpl;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.suppose.model.BaseMetaModel;
import tech.powerjob.common.enums.InstanceStatus;
import tech.powerjob.server.solon.anno.button.JobInstanceButtonService;

import java.time.LocalDateTime;

/**
 * 任务运行日志表
 *
 * @author tjq
 * @since 2020/3/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "任务运行日志", canRemove = false, annoPermission = @AnnoPermission(enable = true, baseCode = "pj_instance_info", baseCodeTranslate = "任务运行日志"),
    annoOrder = @AnnoOrder(orderType = "desc", orderValue = "createTime"))
@NoArgsConstructor
@AllArgsConstructor
@Table("pj_instance_info")
public class InstanceInfoDO extends BaseMetaModel {

    /**
     * 任务ID
     */
    @AnnoField(title = "任务", search = @AnnoSearch(), dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(optionAnno = @AnnoOptionType.OptionAnnoClass(annoClass = JobInfoDO.class, labelKey = "jobName")))
    private String jobId;
    /**
     * 任务所属应用的ID，冗余提高查询效率
     */
    @AnnoField(title = "所属应用", search = @AnnoSearch(), dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(optionAnno = @AnnoOptionType.OptionAnnoClass(annoClass = AppInfoDO.class, labelKey = "appName")))
    private String appId;
    /**
     * TODO 任务实例ID，是否可以删掉？
     */
    @AnnoField(title = "任务实例ID", search = @AnnoSearch())
    private String instanceId;
    /**
     * 任务参数（静态）
     *
     * @since 2021/2/01
     */
    @AnnoField(title = "任务参数（静态）", dataType = AnnoDataType.CODE_EDITOR, show = false)
    private String jobParams;
    /**
     * 任务实例参数（动态）
     */
    @AnnoField(title = "任务实例参数（动态）", dataType = AnnoDataType.CODE_EDITOR, show = false)
    private String instanceParams;
    /**
     * 该任务实例的类型，普通/工作流（InstanceType）
     */
    @AnnoField(title = "类型", dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "普通", value = "1"),
            @AnnoOptionType.OptionData(label = "工作流", value = "2")
        }), search = @AnnoSearch())
    private Integer type;
    /**
     * 该任务实例所属的 workflow ID，仅 workflow 任务存在
     */
    @AnnoField(title = "工作流id", search = @AnnoSearch())
    private String wfInstanceId;
    /**
     * 任务状态 {@link InstanceStatus}
     */
    @AnnoField(title = "任务状态", dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "等待派发", value = "1"),
            @AnnoOptionType.OptionData(label = "等待Worker接收", value = "2"),
            @AnnoOptionType.OptionData(label = "运行中", value = "3"),
            @AnnoOptionType.OptionData(label = "失败", value = "4"),
            @AnnoOptionType.OptionData(label = "成功", value = "5"),
            @AnnoOptionType.OptionData(label = "取消", value = "9"),
            @AnnoOptionType.OptionData(label = "手动停止", value = "10")
        }), search = @AnnoSearch())
    private Integer status;
    /**
     * 执行结果（允许存储稍大的结果）
     */
    @AnnoField(title = "执行结果", dataType = AnnoDataType.CODE_EDITOR, show = false)
    private String result;
    /**
     * 预计触发时间
     */
    @AnnoField(title = "预计触发时间", show = false)
    private Long expectedTriggerTime;
    /**
     * 实际触发时间
     */
    @AnnoField(title = "实际触发时间", dataType = AnnoDataType.DATETIME)
    private LocalDateTime actualTriggerTime;
    /**
     * 结束时间
     */
    @AnnoField(title = "结束时间", dataType = AnnoDataType.DATETIME)
    private LocalDateTime finishedTime;
    /**
     * 最后上报时间
     */
    @AnnoField(title = "最后上报时间", show = false)
    private Long lastReportTime;
    /**
     * TaskTracker 地址
     */
    @AnnoField(title = "TaskTracker地址", show = false)
    private String taskTrackerAddress;
    /**
     * 总共执行的次数（用于重试判断）
     */
    @AnnoField(title = "总共执行次数", show = false)
    private Long runningTimes;

    @AnnoButton(name = "日志", permissionCode = "fetchInstanceLog", annoTpl = @AnnoTpl())
    private Object fetchInstanceLogButton;

    @AnnoButton(name = "重试", permissionCode = "instanceRetry", javaCmd = @AnnoButton.JavaCmd(beanClass = JobInstanceButtonService.class, methodName = "retryInstance"))
    private Object instanceRetryButton;

}
