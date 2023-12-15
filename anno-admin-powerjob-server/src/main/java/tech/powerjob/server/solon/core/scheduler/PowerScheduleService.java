package tech.powerjob.server.solon.core.scheduler;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.wood.annotation.Db;
import tech.powerjob.common.enums.InstanceStatus;
import tech.powerjob.common.enums.TimeExpressionType;
import tech.powerjob.common.model.LifeCycle;
import tech.powerjob.common.utils.CollectionUtils;
import tech.powerjob.server.solon.common.constants.SwitchableStatus;
import tech.powerjob.server.solon.common.timewheel.holder.InstanceTimeWheelService;
import tech.powerjob.server.solon.core.DispatchService;
import tech.powerjob.server.solon.core.instance.InstanceService;
import tech.powerjob.server.solon.core.service.JobService;
import tech.powerjob.server.solon.core.workflow.WorkflowInstanceManager;
import tech.powerjob.server.solon.persistence.remote.model.InstanceInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.JobInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.WorkflowInfoDO;
import tech.powerjob.server.solon.persistence.remote.repository.AppInfoRepository;
import tech.powerjob.server.solon.persistence.remote.repository.InstanceInfoRepository;
import tech.powerjob.server.solon.persistence.remote.repository.JobInfoRepository;
import tech.powerjob.server.solon.persistence.remote.repository.WorkflowInfoRepository;
import tech.powerjob.server.solon.remote.transporter.TransportService;
import tech.powerjob.server.solon.remote.worker.WorkerClusterManagerService;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 任务调度执行服务（调度 CRON 表达式的任务进行执行）
 * 原：FIX_RATE和FIX_DELAY任务不需要被调度，创建后直接被派发到Worker执行，只需要失败重试机制（在InstanceStatusCheckService中完成）
 * 先：那样写不太优雅，东一坨代码西一坨代码的，还是牺牲点性能统一调度算了 （优雅，永不过时～ BY：青钢影）
 *
 * @author tjq
 * @since 2020/4/5
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PowerScheduleService {

    /**
     * 每次并发调度的应用数量
     */
    private static final int MAX_APP_NUM = 10;

    @Inject
    private TransportService transportService;
    @Inject
    private DispatchService dispatchService;
    @Inject
    private InstanceService instanceService;
    @Inject
    private WorkflowInstanceManager workflowInstanceManager;
    @Db
    private AppInfoRepository appInfoRepository;
    @Db
    private JobInfoRepository jobInfoRepository;
    @Db
    private WorkflowInfoRepository workflowInfoRepository;
    @Db
    private InstanceInfoRepository instanceInfoRepository;
    @Inject
    private JobService jobService;
    @Inject
    private TimingStrategyService timingStrategyService;

    public static final long SCHEDULE_RATE = 15000;


    public void scheduleNormalJob(TimeExpressionType timeExpressionType) {
        long start = System.currentTimeMillis();
        // 调度 CRON 表达式 JOB
        try {
            final List<String> allAppIds = appInfoRepository.listAppIdByCurrentServer(transportService.defaultProtocol().getAddress());
            if (CollectionUtils.isEmpty(allAppIds)) {
                log.debug("[NormalScheduler] current server has no app's job to schedule.");
                return;
            }
            scheduleNormalJob0(timeExpressionType, allAppIds);
        } catch (Exception e) {
            log.error("[NormalScheduler] schedule cron job failed.", e);
        }
        long cost = System.currentTimeMillis() - start;
        log.info("[NormalScheduler] {} job schedule use {} ms.", timeExpressionType, cost);
        if (cost > SCHEDULE_RATE) {
            log.warn("[NormalScheduler] The database query is using too much time({}ms), please check if the database load is too high!", cost);
        }
    }

    public void scheduleCronWorkflow() {
        long start = System.currentTimeMillis();
        // 调度 CRON 表达式 WORKFLOW
        try {
            final List<String> allAppIds = appInfoRepository.listAppIdByCurrentServer(transportService.defaultProtocol().getAddress());
            if (CollectionUtils.isEmpty(allAppIds)) {
                log.debug("[CronWorkflowSchedule] current server has no app's workflow to schedule.");
                return;
            }
            scheduleWorkflowCore(allAppIds);
        } catch (Exception e) {
            log.error("[CronWorkflowSchedule] schedule cron workflow failed.", e);
        }
        long cost = System.currentTimeMillis() - start;
        log.info("[CronWorkflowSchedule] cron workflow schedule use {} ms.", cost);
        if (cost > SCHEDULE_RATE) {
            log.warn("[CronWorkflowSchedule] The database query is using too much time({}ms), please check if the database load is too high!", cost);
        }
    }


    public void scheduleFrequentJob() {
        long start = System.currentTimeMillis();
        // 调度 FIX_RATE/FIX_DELAY 表达式 JOB
        try {
            final List<String> allAppIds = appInfoRepository.listAppIdByCurrentServer(transportService.defaultProtocol().getAddress());
            if (CollectionUtils.isEmpty(allAppIds)) {
                log.debug("[FrequentJobSchedule] current server has no app's job to schedule.");
                return;
            }
            scheduleFrequentJobCore(allAppIds);
        } catch (Exception e) {
            log.error("[FrequentJobSchedule] schedule frequent job failed.", e);
        }
        long cost = System.currentTimeMillis() - start;
        log.info("[FrequentJobSchedule] frequent job schedule use {} ms.", cost);
        if (cost > SCHEDULE_RATE) {
            log.warn("[FrequentJobSchedule] The database query is using too much time({}ms), please check if the database load is too high!", cost);
        }
    }


    public void cleanData() {
        try {
            final List<String> allAppIds = appInfoRepository.listAppIdByCurrentServer(transportService.defaultProtocol().getAddress());
            if (allAppIds.isEmpty()) {
                return;
            }
            WorkerClusterManagerService.clean(allAppIds);
        } catch (Exception e) {
            log.error("[CleanData] clean data failed.", e);
        }
    }

    /**
     * 调度普通服务端计算表达式类型（CRON、DAILY_TIME_INTERVAL）的任务
     *
     * @param timeExpressionType 表达式类型
     * @param appIds             appIds
     */
    private void scheduleNormalJob0(TimeExpressionType timeExpressionType, List<String> appIds) {

        long nowTime = System.currentTimeMillis();
        long timeThreshold = nowTime + 2 * SCHEDULE_RATE;
        Lists.partition(appIds, MAX_APP_NUM).forEach(partAppIds -> {

            try {

                // 查询条件：任务开启 + 使用CRON表达调度时间 + 指定appId + 即将需要调度执行
                List<JobInfoDO> jobInfos = jobInfoRepository.findByAppIdInAndStatusAndTimeExpressionTypeAndNextTriggerTimeLessThanEqual(partAppIds, SwitchableStatus.ENABLE.getV(), timeExpressionType.getV(), timeThreshold);

                if (CollectionUtils.isEmpty(jobInfos)) {
                    return;
                }

                // 1. 批量写日志表
                Map<String, String> jobId2InstanceId = Maps.newHashMap();
                log.info("[NormalScheduler] These {} jobs will be scheduled: {}.", timeExpressionType.name(), jobInfos);

                List<InstanceInfoDO> instanceInfoDOList = new ArrayList<>(jobInfos.size());
                jobInfos.forEach(jobInfo -> {
                    InstanceInfoDO instanceInfoDO = instanceService.create(jobInfo.getId(), jobInfo.getAppId(), jobInfo.getJobParams(), null, null, jobInfo.getNextTriggerTime());
                    instanceInfoDOList.add(instanceInfoDO);
                    jobId2InstanceId.put(jobInfo.getId(), instanceInfoDO.getInstanceId());
                });
                instanceInfoRepository.insertList(instanceInfoDOList);

                // 2. 推入时间轮中等待调度执行
                jobInfos.forEach(jobInfoDO -> {

                    String instanceId = jobId2InstanceId.get(jobInfoDO.getId());

                    long targetTriggerTime = jobInfoDO.getNextTriggerTime();
                    long delay = 0;
                    if (targetTriggerTime < nowTime) {
                        log.warn("[Job-{}] schedule delay, expect: {}, current: {}", jobInfoDO.getId(), targetTriggerTime, System.currentTimeMillis());
                    } else {
                        delay = targetTriggerTime - nowTime;
                    }

                    InstanceTimeWheelService.schedule(instanceId, delay, () -> dispatchService.dispatch(jobInfoDO, instanceId, Optional.empty(), Optional.empty()));
                });

                // 3. 计算下一次调度时间（忽略5S内的重复执行，即CRON模式下最小的连续执行间隔为 SCHEDULE_RATE ms）
                jobInfos.forEach(jobInfoDO -> {
                    try {
                        refreshJob(timeExpressionType, jobInfoDO);
                    } catch (Exception e) {
                        log.error("[Job-{}] refresh job failed.", jobInfoDO.getId(), e);
                    }
                });

            } catch (Exception e) {
                log.error("[NormalScheduler] schedule {} job failed.", timeExpressionType.name(), e);
            }
        });
    }

    private void scheduleWorkflowCore(List<String> appIds) {

        long nowTime = System.currentTimeMillis();
        long timeThreshold = nowTime + 2 * SCHEDULE_RATE;
        Lists.partition(appIds, MAX_APP_NUM).forEach(partAppIds -> {
            List<WorkflowInfoDO> wfInfos = workflowInfoRepository.findByAppIdInAndStatusAndTimeExpressionTypeAndNextTriggerTimeLessThanEqual(partAppIds, SwitchableStatus.ENABLE.getV(), TimeExpressionType.CRON.getV(), timeThreshold);

            if (CollectionUtils.isEmpty(wfInfos)) {
                return;
            }

            wfInfos.forEach(wfInfo -> {

                // 1. 先生成调度记录，防止不调度的情况发生
                String wfInstanceId = workflowInstanceManager.create(wfInfo, null, wfInfo.getNextTriggerTime(), null);

                // 2. 推入时间轮，准备调度执行
                long delay = wfInfo.getNextTriggerTime() - System.currentTimeMillis();
                if (delay < 0) {
                    log.warn("[Workflow-{}] workflow schedule delay, expect:{}, actual: {}", wfInfo.getId(), wfInfo.getNextTriggerTime(), System.currentTimeMillis());
                    delay = 0;
                }
                InstanceTimeWheelService.schedule(wfInstanceId, delay, () -> workflowInstanceManager.start(wfInfo, wfInstanceId));

                // 3. 重新计算下一次调度时间并更新
                try {
                    refreshWorkflow(wfInfo);
                } catch (Exception e) {
                    log.error("[Workflow-{}] refresh workflow failed.", wfInfo.getId(), e);
                }
            });
        });
    }

    private void scheduleFrequentJobCore(List<String> appIds) {

        Lists.partition(appIds, MAX_APP_NUM).forEach(partAppIds -> {
            try {
                // 查询所有的秒级任务（只包含ID）
                List<String> jobIds = jobInfoRepository.findByAppIdInAndStatusAndTimeExpressionTypeIn(partAppIds, SwitchableStatus.ENABLE.getV(), TimeExpressionType.FREQUENT_TYPES);
                if (CollectionUtils.isEmpty(jobIds)) {
                    return;
                }
                // 查询日志记录表中是否存在相关的任务
                List<String> runningJobIdList = instanceInfoRepository.findByJobIdInAndStatusIn(jobIds, InstanceStatus.GENERALIZED_RUNNING_STATUS);
                Set<String> runningJobIdSet = Sets.newHashSet(runningJobIdList);

                List<String> notRunningJobIds = Lists.newLinkedList();
                jobIds.forEach(jobId -> {
                    if (!runningJobIdSet.contains(jobId)) {
                        notRunningJobIds.add(jobId);
                    }
                });

                if (CollectionUtils.isEmpty(notRunningJobIds)) {
                    return;
                }

                notRunningJobIds.forEach(jobId -> {
                    JobInfoDO jobInfoDO = jobInfoRepository.selectById(jobId);
                    if (jobInfoDO != null) {
                        LifeCycle lifeCycle = LifeCycle.parse(jobInfoDO.getLifecycle());
                        // 生命周期已经结束
                        if (lifeCycle.getEnd() != null && lifeCycle.getEnd() < System.currentTimeMillis()) {
                            jobInfoDO.setStatus(SwitchableStatus.DISABLE.getV());
                            jobInfoDO.setUpdateTime(LocalDateTime.now());
                            jobInfoRepository.insert(jobInfoDO);
                            log.info("[FrequentScheduler] disable frequent job,id:{}.", jobInfoDO.getId());
                        } else if (lifeCycle.getStart() == null || lifeCycle.getStart() < System.currentTimeMillis() + SCHEDULE_RATE * 2) {
                            log.info("[FrequentScheduler] schedule frequent job,id:{}.", jobInfoDO.getId());
                            jobService.runJob(jobInfoDO.getAppId(), jobId, null, Optional.ofNullable(lifeCycle.getStart()).orElse(0L) - System.currentTimeMillis());
                        }
                    }
                });
            } catch (Exception e) {
                log.error("[FrequentScheduler] schedule frequent job failed.", e);
            }
        });
    }

    private void refreshJob(TimeExpressionType timeExpressionType, JobInfoDO jobInfo) {
        LifeCycle lifeCycle = LifeCycle.parse(jobInfo.getLifecycle());
        Long nextTriggerTime = timingStrategyService.calculateNextTriggerTime(jobInfo.getNextTriggerTime(), timeExpressionType, jobInfo.getTimeExpression(), lifeCycle.getStart(), lifeCycle.getEnd());

        JobInfoDO updatedJobInfo = new JobInfoDO();
        BeanUtil.copyProperties(jobInfo, updatedJobInfo);

        if (nextTriggerTime == null) {
            log.warn("[Job-{}] this job won't be scheduled anymore, system will set the status to DISABLE!", jobInfo.getId());
            updatedJobInfo.setStatus(SwitchableStatus.DISABLE.getV());
        } else {
            updatedJobInfo.setNextTriggerTime(nextTriggerTime);
        }
        updatedJobInfo.setUpdateTime(LocalDateTime.now());

        jobInfoRepository.updateById(updatedJobInfo);
    }

    private void refreshWorkflow(WorkflowInfoDO wfInfo) {
        LifeCycle lifeCycle = LifeCycle.parse(wfInfo.getLifecycle());
        Long nextTriggerTime = timingStrategyService.calculateNextTriggerTime(wfInfo.getNextTriggerTime(), TimeExpressionType.CRON, wfInfo.getTimeExpression(), lifeCycle.getStart(), lifeCycle.getEnd());

        WorkflowInfoDO updateEntity = new WorkflowInfoDO();
        BeanUtil.copyProperties(wfInfo, updateEntity);

        if (nextTriggerTime == null) {
            log.warn("[Workflow-{}] this workflow won't be scheduled anymore, system will set the status to DISABLE!", wfInfo.getId());
            updateEntity.setStatus(SwitchableStatus.DISABLE.getV());
        } else {
            updateEntity.setNextTriggerTime(nextTriggerTime);
        }

        updateEntity.setUpdateTime(LocalDateTime.now());
        workflowInfoRepository.insert(updateEntity);
    }

}
