package tech.powerjob.server.solon.persistence.remote.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoOrder;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.db.BaseMetaModel;
import site.sorghum.anno.plugin.ao.AnUser;
import tech.powerjob.server.solon.anno.button.JobButtonService;

import java.time.LocalDateTime;

/**
 * 任务信息表
 *
 * @author tjq
 * @since 2020/3/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "任务管理", annoPermission = @AnnoPermission(enable = true, baseCode = "pj_job_info", baseCodeTranslate = "任务管理"),
    annoOrder = @AnnoOrder(orderType = "desc", orderValue = "updateTime"))
@NoArgsConstructor
@AllArgsConstructor
@Table("pj_job_info")
public class JobInfoDO extends BaseMetaModel {

    /* ************************** 任务基本信息 ************************** */
    /**
     * 任务名称
     */
    @AnnoField(title = "任务名称", search = @AnnoSearch(), edit = @AnnoEdit(notNull = true))
    private String jobName;
    /**
     * 任务描述
     */
    @AnnoField(title = "任务描述", edit = @AnnoEdit(), show = false)
    private String jobDescription;
    /**
     * 任务所属的应用ID
     */
    @AnnoField(title = "应用ID", edit = @AnnoEdit(), search = @AnnoSearch(),
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(optionAnno = @AnnoOptionType.OptionAnnoClass(annoClass = AppInfoDO.class, labelKey = "appName")))
    private String appId;
    /**
     * 任务自带的参数
     */
    @AnnoField(title = "任务参数", dataType = AnnoDataType.CODE_EDITOR, edit = @AnnoEdit(), show = false)
    private String jobParams;

    /* ************************** 定时参数 ************************** */
    /**
     * 时间表达式类型（CRON/API/FIX_RATE/FIX_DELAY）
     */
    @AnnoField(title = "时间表达式类型", dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "API", value = "1"),
            @AnnoOptionType.OptionData(label = "CRON", value = "2"),
            @AnnoOptionType.OptionData(label = "固定频率（毫秒）", value = "3"),
            @AnnoOptionType.OptionData(label = "固定延迟（毫秒）", value = "4"),
            @AnnoOptionType.OptionData(label = "工作流", value = "5"),
            @AnnoOptionType.OptionData(label = "每日固定间隔", value = "11")
        }),
        edit = @AnnoEdit(placeHolder = "请选择类型", notNull = true), search = @AnnoSearch())
    private Integer timeExpressionType;
    /**
     * 时间表达式，CRON/NULL/LONG/LONG
     */
    @AnnoField(title = "时间表达式", edit = @AnnoEdit(placeHolder = "CRON 填写 CRON 表达式，秒级任务填写整数，API 无需填写"))
    private String timeExpression;

    /**
     * 任务生命周期（开始和结束时间）
     */
    @AnnoField(title = "任务生命周期", show = false)
    private String lifecycle;

    @AnnoField(title = "任务生命周期-开始时间", virtualColumn = true, dataType = AnnoDataType.DATETIME, edit = @AnnoEdit(), show = false)
    private LocalDateTime lifecycleStart;

    @AnnoField(title = "任务生命周期-结束时间", virtualColumn = true, dataType = AnnoDataType.DATETIME, edit = @AnnoEdit(), show = false)
    private LocalDateTime lifecycleEnd;

    /* ************************** 执行方式 ************************** */
    /**
     * 执行类型，单机/广播/MR
     */
    @AnnoField(title = "执行类型", dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "单机执行", value = "1"),
            @AnnoOptionType.OptionData(label = "广播执行", value = "2"),
            @AnnoOptionType.OptionData(label = "Map 执行", value = "4"),
            @AnnoOptionType.OptionData(label = "MapReduce 执行", value = "3")
        }),
        edit = @AnnoEdit(placeHolder = "请选择类型", notNull = true), search = @AnnoSearch())
    private Integer executeType;
    /**
     * 执行器类型，Java/Shell
     */
    @AnnoField(title = "执行器类型", dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "内建处理器", value = "1"),
            @AnnoOptionType.OptionData(label = "外部处理器（动态加载）", value = "4")
        }), edit = @AnnoEdit(), show = false)
    private Integer processorType;
    /**
     * 执行器信息
     */
    @AnnoField(title = "执行器信息", edit = @AnnoEdit(placeHolder = "全限定类名，eg: tech.powerjob.official.processors.impl.script.ShellProcessor", notNull = true))
    private String processorInfo;

    /**
     * 任务分发策略
     */
    @AnnoField(title = "任务分发策略", dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "健康优先（HEALTH_FIRST）", value = "1"),
            @AnnoOptionType.OptionData(label = "随机（RANDOM）", value = "2")
        }), edit = @AnnoEdit())
    private Integer dispatchStrategy;

    /* ************************** 运行时配置 ************************** */
    /**
     * 最大同时运行任务数，默认 1
     */
    @AnnoField(title = "最大实例数", edit = @AnnoEdit(), show = false)
    private Integer maxInstanceNum;
    /**
     * 并发度，同时执行某个任务的最大线程数量
     */
    @AnnoField(title = "单机线程并发度", edit = @AnnoEdit(), show = false)
    private Integer concurrency;
    /**
     * 任务整体超时时间
     */
    @AnnoField(title = "运行时间限制（毫秒）", edit = @AnnoEdit(), show = false)
    private Long instanceTimeLimit;

    /* ************************** 重试配置 ************************** */

    @AnnoField(title = "Instance 重试次数", edit = @AnnoEdit(), show = false)
    private Integer instanceRetryNum;

    @AnnoField(title = "Task 重试次数", edit = @AnnoEdit(), show = false)
    private Integer taskRetryNum;

    /**
     * 1 正常运行，2 停止（不再调度）
     */
    @AnnoField(title = "任务状态", dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "正常运行", value = "1"),
            @AnnoOptionType.OptionData(label = "停止（不再调度）", value = "2")
        }), search = @AnnoSearch(), edit = @AnnoEdit())
    private Integer status;
    /**
     * 下一次调度时间
     */
    @AnnoField(title = "下一次调度时间", show = false)
    private Long nextTriggerTime;
    /* ************************** 繁忙机器配置 ************************** */
    /**
     * 最低CPU核心数量，0代表不限
     */
    @AnnoField(title = "最低 CPU 核心数", edit = @AnnoEdit(), show = false)
    private Double minCpuCores;
    /**
     * 最低内存空间，单位 GB，0代表不限
     */
    @AnnoField(title = "最低内存(GB)", edit = @AnnoEdit(), show = false)
    private Double minMemorySpace;
    /**
     * 最低磁盘空间，单位 GB，0代表不限
     */
    @AnnoField(title = "最低磁盘空间(GB)", edit = @AnnoEdit(), show = false)
    private Double minDiskSpace;
    /* ************************** 集群配置 ************************** */
    /**
     * 指定机器运行，空代表不限，非空则只会使用其中的机器运行（多值逗号分割）
     */
    @AnnoField(title = "执行机器地址", edit = @AnnoEdit(placeHolder = "执行机器地址（可选，不指定代表全部；多值英文逗号分割）"), show = false)
    private String designatedWorkers;
    /**
     * 最大机器数量
     */
    @AnnoField(title = "最大执行机器数量", edit = @AnnoEdit(), show = false)
    private Integer maxWorkerCount;
    /**
     * 报警用户ID列表，多值逗号分隔
     */
    @AnnoField(title = "报警通知用户", edit = @AnnoEdit(placeHolder = "可以多选"), show = false,
        dataType = AnnoDataType.OPTIONS, optionType = @AnnoOptionType(optionAnno = @AnnoOptionType.OptionAnnoClass(annoClass = AnUser.class), isMultiple = true))
    private String notifyUserIds;

    /**
     * 扩展参数，PowerJob 自身不会使用该数据，留给开发者扩展时使用
     * 比如 WorkerFilter 的自定义 worker 过滤逻辑，可在此传入过滤指标 GpuUsage < 10
     */
    @AnnoField(title = "扩展参数", dataType = AnnoDataType.CODE_EDITOR, show = false)
    private String extra;

    /**
     * 告警配置
     */
    @AnnoField(title = "告警配置", show = false)
    private String alarmConfig;

    @AnnoField(title = "错误阈值", edit = @AnnoEdit(), show = false, virtualColumn = true)
    private Integer alertThreshold;

    @AnnoField(title = "统计窗口(s)", edit = @AnnoEdit(), show = false, virtualColumn = true)
    private Integer statisticWindowLen;

    @AnnoField(title = "沉默窗口(s)", edit = @AnnoEdit(), show = false, virtualColumn = true)
    private Integer silenceWindowLen;

    /**
     * 任务归类，开放给接入方自由定制
     */
    @AnnoField(title = "任务归类", show = false)
    private String tag;

    /**
     * 日志配置，包括日志级别、日志方式等配置信息
     */
    @AnnoField(title = "日志配置", show = false)
    private String logConfig;

    @AnnoField(title = "日志类型", virtualColumn = true, dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "ONLINE", value = "1"),
            @AnnoOptionType.OptionData(label = "LOCAL", value = "2"),
            @AnnoOptionType.OptionData(label = "STDOUT", value = "3"),
            @AnnoOptionType.OptionData(label = "LOCAL_AND_ONLINE", value = "4"),
            @AnnoOptionType.OptionData(label = "NULL", value = "999")
        }), edit = @AnnoEdit(), show = false)
    private Integer type;

    @AnnoField(title = "日志级别", virtualColumn = true, dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "DEBUG", value = "1"),
            @AnnoOptionType.OptionData(label = "INFO", value = "2"),
            @AnnoOptionType.OptionData(label = "WARN", value = "3"),
            @AnnoOptionType.OptionData(label = "ERROR", value = "4"),
            @AnnoOptionType.OptionData(label = "OFF", value = "99")
        }), edit = @AnnoEdit(), show = false)
    private Integer level;

    @AnnoField(title = "loggerName", edit = @AnnoEdit(), show = false, virtualColumn = true)
    private String loggerName;

    @AnnoButton(name = "运行", permissionCode = "run", javaCmd = @AnnoButton.JavaCmd(runSupplier = JobButtonService.class))
    private Object runButton;
}
