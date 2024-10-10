package site.sorghum.anno.delaykit.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoOrder;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.enums.TrueFalseEnum;
import site.sorghum.anno.db.BaseMetaModel;

import java.io.Serializable;
import java.util.Date;

@AnnoMain(
    name = "任务执行记录",
    tableName = "delay_kit_job_history",
    canRemove = false,
    annoOrder = @AnnoOrder(
        orderValue = "id",
        orderType = "desc"
    )
)
@Data
@EqualsAndHashCode(callSuper = false)
public class JobHistoryAo extends BaseMetaModel implements Serializable {

    @AnnoField(
        title = "任务ID",
        tableFieldName = "job_id"
    )
    String jobId;

    @AnnoField(
        title = "执行结果",
        tableFieldName = "status",
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(
            optionEnum = TrueFalseEnum.class
        )
    )
    Integer status;

    @AnnoField(
        title = "任务名称",
        tableFieldName = "job_name",
        search = @AnnoSearch
    )
    String jobName;

    @AnnoField(
        title = "执行服务",
        tableFieldName = "service"
    )
    String service;

    @AnnoField(
        title = "远程地址",
        tableFieldName = "remote_addr"
    )
    String remoteAddr;

    @AnnoField(
        title = "远程参数",
        tableFieldName = "remote_params",
        dataType = AnnoDataType.CODE_EDITOR
    )
    String remoteParams;

    @AnnoField(
        title = "执行结果",
        tableFieldName = "result",
        dataType = AnnoDataType.CODE_EDITOR
    )
    String result;

    @AnnoField(
        title = "异常信息",
        tableFieldName = "exception",
        dataType = AnnoDataType.CODE_EDITOR
    )
    String exception;

    @AnnoField(
        title = "执行次数",
        tableFieldName = "now_count"
    )
    Integer nowCount;

    @AnnoField(
        title = "最大执行次数",
        tableFieldName = "max_count"
    )
    Integer maxCount;

    @AnnoField(
        title = "延迟时间",
        tableFieldName = "interval"
    )
    Integer interval;

    @AnnoField(
        title = "上次执行时间",
        tableFieldName = "last_run_time"
    )
    Date lastRunTime;

    @AnnoField(
        title = "首次执行时间",
        tableFieldName = "first_run_time"
    )
    Date firstRunTime;


    @AnnoField(
        title = "本次执行时间",
        tableFieldName = "run_time"
    )
    Date runTime;
}
