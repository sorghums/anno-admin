package tech.powerjob.server.solon.core.service.impl.job;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.wood.annotation.Db;
import tech.powerjob.server.solon.common.SJ;
import tech.powerjob.server.solon.common.constants.SwitchableStatus;
import tech.powerjob.server.solon.common.timewheel.holder.InstanceTimeWheelService;
import tech.powerjob.server.solon.core.DispatchService;
import tech.powerjob.server.solon.core.instance.InstanceService;
import tech.powerjob.server.solon.core.scheduler.TimingStrategyService;
import tech.powerjob.server.solon.core.service.JobService;
import tech.powerjob.server.solon.persistence.remote.model.InstanceInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.JobInfoDO;
import tech.powerjob.server.solon.persistence.remote.repository.InstanceInfoRepository;
import tech.powerjob.server.solon.persistence.remote.repository.JobInfoRepository;
import tech.powerjob.server.solon.remote.server.redirector.DesignateServer;
import tech.powerjob.common.PowerQuery;
import tech.powerjob.common.enums.InstanceStatus;
import tech.powerjob.common.enums.TimeExpressionType;
import tech.powerjob.common.exception.PowerJobException;
import tech.powerjob.common.model.AlarmConfig;
import tech.powerjob.common.model.LifeCycle;
import tech.powerjob.common.request.http.SaveJobInfoRequest;
import tech.powerjob.common.response.JobInfoDTO;
import tech.powerjob.common.serialize.JsonUtils;
import tech.powerjob.common.utils.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JobServiceImpl
 *
 * @author tjq
 * @since 2023/3/4
 */
@Slf4j
@Component
public class JobServiceImpl implements JobService {

    @Inject
    private InstanceService instanceService;
    @Inject
    private DispatchService dispatchService;
    @Db
    private JobInfoRepository jobInfoRepository;
    @Db
    private InstanceInfoRepository instanceInfoRepository;
    @Inject
    private TimingStrategyService timingStrategyService;

    /**
     * 保存/修改任务
     *
     * @param request 任务请求
     * @return 创建的任务ID（jobId）
     */
    @Override
    public String saveJob(SaveJobInfoRequest request) {

        request.valid();

        JobInfoDO jobInfoDO;
        if (request.getId() != null) {
            jobInfoDO = getJobInfoDO(String.valueOf(request.getId()));
        } else {
            jobInfoDO = new JobInfoDO();
        }

        // 值拷贝
        BeanUtil.copyProperties(request, jobInfoDO);

        // 拷贝枚举值
        jobInfoDO.setExecuteType(request.getExecuteType().getV());
        jobInfoDO.setProcessorType(request.getProcessorType().getV());
        jobInfoDO.setTimeExpressionType(request.getTimeExpressionType().getV());
        jobInfoDO.setStatus(request.isEnable() ? SwitchableStatus.ENABLE.getV() : SwitchableStatus.DISABLE.getV());
        jobInfoDO.setDispatchStrategy(request.getDispatchStrategy().getV());

        // 填充默认值，非空保护防止 NPE
        fillDefaultValue(jobInfoDO);

        // 转化报警用户列表
        if (request.getNotifyUserIds() != null) {
            if (request.getNotifyUserIds().size() == 0) {
                jobInfoDO.setNotifyUserIds(null);
            } else {
                jobInfoDO.setNotifyUserIds(SJ.COMMA_JOINER.join(request.getNotifyUserIds()));
            }
        }
        LifeCycle lifecycle = Optional.ofNullable(request.getLifeCycle()).orElse(LifeCycle.EMPTY_LIFE_CYCLE);
        jobInfoDO.setLifecycle(JSON.toJSONString(lifecycle));
        // 检查定时策略
        timingStrategyService.validate(request.getTimeExpressionType(), request.getTimeExpression(), lifecycle.getStart(), lifecycle.getEnd());
        calculateNextTriggerTime(jobInfoDO);
        if (request.getId() == null) {
            jobInfoDO.setUpdateTime(LocalDateTime.now());
        }
        // 检查告警配置
        if (request.getAlarmConfig() != null) {
            AlarmConfig config = request.getAlarmConfig();
            if (config.getStatisticWindowLen() == null || config.getAlertThreshold() == null || config.getSilenceWindowLen() == null) {
                throw new PowerJobException("illegal alarm config!");
            }
            jobInfoDO.setAlarmConfig(JSON.toJSONString(request.getAlarmConfig()));
        }
        // 日志配置
        if (request.getLogConfig() != null) {
            jobInfoDO.setLogConfig(JSONObject.toJSONString(request.getLogConfig()));
        }
        jobInfoRepository.insert(jobInfoDO);
        return jobInfoDO.getId();
    }

    /**
     * 复制任务
     *
     * @param jobId 目标任务ID
     * @return 复制后的任务 ID
     */
    @Override
    public JobInfoDO copyJob(String jobId) {

        JobInfoDO origin = jobInfoRepository.selectById(jobId);
        if (origin == null) {
            throw new IllegalArgumentException("can't find job by jobId: " + jobId);
        }
        if (origin.getStatus() == SwitchableStatus.DELETED.getV()) {
            throw new IllegalStateException("can't copy the job which has been deleted!");
        }
        JobInfoDO copyJob = new JobInfoDO();
        // 值拷贝
        BeanUtil.copyProperties(origin, copyJob);
        // 填充默认值，理论上应该不需要
        fillDefaultValue(copyJob);
        // 修正创建时间以及更新时间
        copyJob.setId(null);
        copyJob.setJobName(copyJob.getJobName() + "_COPY");
        LocalDateTime now = LocalDateTime.now();
        copyJob.setCreateTime(now);
        copyJob.setUpdateTime(now);

        jobInfoRepository.insert(copyJob);
        return copyJob;

    }

    @Override
    public JobInfoDTO fetchJob(String jobId) {
        JobInfoDO jobInfoDO = jobInfoRepository.selectById(jobId);
        if (jobInfoDO == null) {
            throw new IllegalArgumentException("can't find job by jobId: " + jobId);
        }
        return JobConverter.convertJobInfoDO2JobInfoDTO(jobInfoRepository.selectById(jobId));
    }

    @Override
    public List<JobInfoDTO> fetchAllJob(String appId) {
        return jobInfoRepository.findByAppId(appId).stream().map(JobConverter::convertJobInfoDO2JobInfoDTO).collect(Collectors.toList());
    }

    @Override
    public List<JobInfoDTO> queryJob(PowerQuery powerQuery) {
        log.error("queryJob is not supported now!");
        return null;
    }

    /**
     * 手动立即运行某个任务
     *
     * @param jobId          任务ID
     * @param instanceParams 任务实例参数（仅 OpenAPI 存在）
     * @param delay          延迟时间，单位 毫秒
     * @return 任务实例ID
     */
    @Override
    @DesignateServer
    public String runJob(String appId, String jobId, String instanceParams, Long delay) {

        delay = delay == null ? 0 : delay;
        JobInfoDO jobInfo = jobInfoRepository.selectById(jobId);
        if (jobInfo == null) {
            throw new IllegalArgumentException("can't find job by jobId: " + jobId);
        }

        log.info("[Job-{}] try to run job in app[{}], instanceParams={},delay={} ms.", jobInfo.getId(), appId, instanceParams, delay);
        final InstanceInfoDO instanceInfo = instanceService.create(jobInfo.getId(), jobInfo.getAppId(), jobInfo.getJobParams(), instanceParams, null, System.currentTimeMillis() + Math.max(delay, 0));
        instanceInfoRepository.insert(instanceInfo);
        if (delay <= 0) {
            dispatchService.dispatch(jobInfo, instanceInfo.getInstanceId(), Optional.of(instanceInfo), Optional.empty());
        } else {
            InstanceTimeWheelService.schedule(instanceInfo.getInstanceId(), delay, () -> dispatchService.dispatch(jobInfo, instanceInfo.getInstanceId(), Optional.empty(), Optional.empty()));
        }
        log.info("[Job-{}|{}] execute 'runJob' successfully, params={}", jobInfo.getId(), instanceInfo.getInstanceId(), instanceParams);
        return instanceInfo.getInstanceId();
    }


    /**
     * 删除某个任务
     *
     * @param jobId 任务ID
     */
    @Override
    public void deleteJob(String jobId) {
        shutdownOrStopJob(jobId, SwitchableStatus.DELETED);
    }

    /**
     * 禁用某个任务
     */
    @Override
    public void disableJob(String jobId) {
        shutdownOrStopJob(jobId, SwitchableStatus.DISABLE);
    }

    /**
     * 导出某个任务为 JSON
     *
     * @param jobId jobId
     * @return 导出结果
     */
    @Override
    public SaveJobInfoRequest exportJob(String jobId) {
        JobInfoDO jobInfoDO = getJobInfoDO(jobId);

        final SaveJobInfoRequest saveJobInfoRequest = JobConverter.convertJobInfoDO2SaveJobInfoRequest(jobInfoDO);
        saveJobInfoRequest.setId(null);
        saveJobInfoRequest.setJobName(saveJobInfoRequest.getJobName() + "_EXPORT_" + System.currentTimeMillis());
        log.info("[Job-{}] [exportJob] jobInfoDO: {}, saveJobInfoRequest: {}", jobId, JsonUtils.toJSONString(jobInfoDO), JsonUtils.toJSONString(saveJobInfoRequest));
        return saveJobInfoRequest;
    }

    /**
     * 启用某个任务
     *
     * @param jobId 任务ID
     */
    @Override
    public void enableJob(String jobId) {
        JobInfoDO jobInfoDO = getJobInfoDO(jobId);

        jobInfoDO.setStatus(SwitchableStatus.ENABLE.getV());
        calculateNextTriggerTime(jobInfoDO);

        jobInfoRepository.updateById(jobInfoDO);
    }

    /**
     * 停止或删除某个JOB
     * 秒级任务还要额外停止正在运行的任务实例
     */
    private void shutdownOrStopJob(String jobId, SwitchableStatus status) {

        // 1. 先更新 job_info 表
        JobInfoDO jobInfoDO = getJobInfoDO(jobId);
        jobInfoDO.setStatus(status.getV());
        jobInfoDO.setUpdateTime(LocalDateTime.now());
        jobInfoRepository.updateById(jobInfoDO);

        // 2. 关闭秒级任务
        if (!TimeExpressionType.FREQUENT_TYPES.contains(jobInfoDO.getTimeExpressionType())) {
            return;
        }
        List<InstanceInfoDO> executeLogs = instanceInfoRepository.findByJobIdAndStatusIn(jobId, InstanceStatus.GENERALIZED_RUNNING_STATUS);
        if (CollectionUtils.isEmpty(executeLogs)) {
            return;
        }
        if (executeLogs.size() > 1) {
            log.warn("[Job-{}] frequent job should just have one running instance, there must have some bug.", jobId);
        }
        executeLogs.forEach(instance -> {
            try {
                // 重复查询了数据库，不过问题不大，这个调用量很小
                instanceService.stopInstance(instance.getAppId(), instance.getInstanceId());
            } catch (Exception ignore) {
                // ignore exception
            }
        });
    }

    private JobInfoDO getJobInfoDO(String jobId) {
        JobInfoDO jobInfoDO = jobInfoRepository.selectById(jobId);
        if (jobInfoDO == null) {
            throw new IllegalArgumentException("can't find job by jobId:" + jobId);
        }
        return jobInfoDO;
    }

    private void calculateNextTriggerTime(JobInfoDO jobInfo) {
        // 计算下次调度时间
        if (TimeExpressionType.FREQUENT_TYPES.contains(jobInfo.getTimeExpressionType())) {
            // 固定频率类型的任务不计算
            jobInfo.setNextTriggerTime(null);
        } else {
            LifeCycle lifeCycle = LifeCycle.parse(jobInfo.getLifecycle());
            Long nextValidTime = timingStrategyService.calculateNextTriggerTimeWithInspection(TimeExpressionType.of(jobInfo.getTimeExpressionType()), jobInfo.getTimeExpression(), lifeCycle.getStart(), lifeCycle.getEnd());
            jobInfo.setNextTriggerTime(nextValidTime);
        }
        // 重写最后修改时间
        jobInfo.setUpdateTime(LocalDateTime.now());
    }

    private void fillDefaultValue(JobInfoDO jobInfoDO) {
        if (jobInfoDO.getMaxWorkerCount() == null) {
            jobInfoDO.setMaxWorkerCount(0);
        }
        if (jobInfoDO.getMaxInstanceNum() == null) {
            jobInfoDO.setMaxInstanceNum(0);
        }
        if (jobInfoDO.getConcurrency() == null) {
            jobInfoDO.setConcurrency(5);
        }
        if (jobInfoDO.getInstanceRetryNum() == null) {
            jobInfoDO.setInstanceRetryNum(0);
        }
        if (jobInfoDO.getTaskRetryNum() == null) {
            jobInfoDO.setTaskRetryNum(0);
        }
        if (jobInfoDO.getInstanceTimeLimit() == null) {
            jobInfoDO.setInstanceTimeLimit(0L);
        }
    }
}
