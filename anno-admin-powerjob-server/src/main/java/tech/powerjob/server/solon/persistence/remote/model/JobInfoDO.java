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

/**
 * 任务信息表
 *
 * @author tjq
 * @since 2020/3/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "任务信息表")
@NoArgsConstructor
@AllArgsConstructor
@Table("pj_job_info")
public class JobInfoDO extends BaseMetaModel {

    /* ************************** 任务基本信息 ************************** */
    /**
     * 任务名称
     */
    @AnnoField(title = "任务名称")
    private String jobName;
    /**
     * 任务描述
     */
    @AnnoField(title = "任务描述")
    private String jobDescription;
    /**
     * 任务所属的应用ID
     */
    @AnnoField(title = "任务所属的应用ID")
    private String appId;
    /**
     * 任务自带的参数
     */
    @AnnoField(title = "任务自带的参数", dataType = AnnoDataType.CODE_EDITOR)
    private String jobParams;

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

    /* ************************** 执行方式 ************************** */
    /**
     * 执行类型，单机/广播/MR
     */
    @AnnoField(title = "执行类型")
    private Integer executeType;
    /**
     * 执行器类型，Java/Shell
     */
    @AnnoField(title = "执行器类型")
    private Integer processorType;
    /**
     * 执行器信息
     */
    @AnnoField(title = "执行器信息")
    private String processorInfo;

    /* ************************** 运行时配置 ************************** */
    /**
     * 最大同时运行任务数，默认 1
     */
    @AnnoField(title = "最大同时运行任务数")
    private Integer maxInstanceNum;
    /**
     * 并发度，同时执行某个任务的最大线程数量
     */
    @AnnoField(title = "并发度")
    private Integer concurrency;
    /**
     * 任务整体超时时间
     */
    @AnnoField(title = "任务整体超时时间")
    private Long instanceTimeLimit;

    /* ************************** 重试配置 ************************** */

    @AnnoField(title = "任务实例重试次数")
    private Integer instanceRetryNum;

    @AnnoField(title = "任务重试次数")
    private Integer taskRetryNum;

    /**
     * 1 正常运行，2 停止（不再调度）
     */
    @AnnoField(title = "任务状态")
    private Integer status;
    /**
     * 下一次调度时间
     */
    @AnnoField(title = "下一次调度时间")
    private Long nextTriggerTime;
    /* ************************** 繁忙机器配置 ************************** */
    /**
     * 最低CPU核心数量，0代表不限
     */
    @AnnoField(title = "最低CPU核心数量")
    private Double minCpuCores;
    /**
     * 最低内存空间，单位 GB，0代表不限
     */
    @AnnoField(title = "最低内存空间")
    private Double minMemorySpace;
    /**
     * 最低磁盘空间，单位 GB，0代表不限
     */
    @AnnoField(title = "最低磁盘空间")
    private Double minDiskSpace;
    /* ************************** 集群配置 ************************** */
    /**
     * 指定机器运行，空代表不限，非空则只会使用其中的机器运行（多值逗号分割）
     */
    @AnnoField(title = "指定机器运行")
    private String designatedWorkers;
    /**
     * 最大机器数量
     */
    @AnnoField(title = "最大机器数量")
    private Integer maxWorkerCount;
    /**
     * 报警用户ID列表，多值逗号分隔
     */
    @AnnoField(title = "报警用户列表")
    private String notifyUserIds;

    /**
     * 扩展参数，PowerJob 自身不会使用该数据，留给开发者扩展时使用
     * 比如 WorkerFilter 的自定义 worker 过滤逻辑，可在此传入过滤指标 GpuUsage < 10
     */
    @AnnoField(title = "扩展参数", dataType = AnnoDataType.CODE_EDITOR)
    private String extra;

    /**
     * 任务分发策略
     */
    @AnnoField(title = "任务分发策略")
    private Integer dispatchStrategy;

    /**
     * 任务生命周期（开始和结束时间）
     */
    @AnnoField(title = "任务生命周期")
    private String lifecycle;
    /**
     * 告警配置
     */
    @AnnoField(title = "告警配置")
    private String alarmConfig;

    /**
     * 任务归类，开放给接入方自由定制
     */
    @AnnoField(title = "任务归类")
    private String tag;

    /**
     * 日志配置，包括日志级别、日志方式等配置信息
     */
    @AnnoField(title = "日志配置")
    private String logConfig;
}
