package tech.powerjob.server.solon.core.instance;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.wood.annotation.Db;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno.plugin.ao.AnUser;
import site.sorghum.anno.plugin.dao.AnUserDao;
import tech.powerjob.common.enums.InstanceStatus;
import tech.powerjob.common.enums.TimeExpressionType;
import tech.powerjob.common.model.LifeCycle;
import tech.powerjob.common.request.ServerStopInstanceReq;
import tech.powerjob.common.request.TaskTrackerReportInstanceStatusReq;
import tech.powerjob.common.utils.CommonUtils;
import tech.powerjob.remote.framework.base.URL;
import tech.powerjob.server.solon.anno.utils.IdConvertUtil;
import tech.powerjob.server.solon.common.module.WorkerInfo;
import tech.powerjob.server.solon.common.timewheel.holder.HashedWheelTimerHolder;
import tech.powerjob.server.solon.core.alarm.AlarmCenter;
import tech.powerjob.server.solon.core.alarm.AlarmUtils;
import tech.powerjob.server.solon.core.alarm.module.JobInstanceAlarm;
import tech.powerjob.server.solon.core.workflow.WorkflowInstanceManager;
import tech.powerjob.server.solon.persistence.remote.model.InstanceInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.JobInfoDO;
import tech.powerjob.server.solon.persistence.remote.repository.InstanceInfoRepository;
import tech.powerjob.server.solon.remote.aware.TransportServiceAware;
import tech.powerjob.server.solon.remote.transporter.TransportService;
import tech.powerjob.server.solon.remote.transporter.impl.ServerURLFactory;
import tech.powerjob.server.solon.remote.worker.WorkerClusterQueryService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 管理被调度的任务实例（状态更新相关）
 *
 * @author tjq
 * @since 2020/4/7
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InstanceManager implements TransportServiceAware {

    @Inject
    private AlarmCenter alarmCenter;

    @Inject
    private InstanceLogService instanceLogService;

    @Inject
    private InstanceMetadataService instanceMetadataService;

    @Db
    private InstanceInfoRepository instanceInfoRepository;

    @Inject
    private WorkflowInstanceManager workflowInstanceManager;

    @Inject
    private WorkerClusterQueryService workerClusterQueryService;

    /**
     * 基础组件通过 aware 注入，避免循环依赖
     */
    private TransportService transportService;

    /**
     * 更新任务状态
     * ********************************************
     * 2021-02-03 modify by Echo009
     * 实例的执行次数统一在这里管理，对于非固定频率的任务
     * 当 db 中实例的状态为等待派发时，runningTimes + 1
     * ********************************************
     *
     * @param req TaskTracker上报任务实例状态的请求
     */
    public void updateStatus(TaskTrackerReportInstanceStatusReq req) throws ExecutionException {

        String instanceId = IdConvertUtil.toString(req.getInstanceId());
        // 获取相关数据
        JobInfoDO jobInfo = instanceMetadataService.fetchJobInfoByInstanceId(req.getInstanceId().toString());
        InstanceInfoDO instanceInfo = instanceInfoRepository.findByInstanceId(instanceId);
        if (instanceInfo == null) {
            log.warn("[InstanceManager-{}] can't find InstanceInfo from database", instanceId);
            return;
        }

        // 考虑极端情况：Processor 处理耗时小于 server 写 DB 耗时，会导致状态上报时无 taskTracker 地址，此处等待后重新从DB获取数据 GitHub#620
        if (StringUtils.isEmpty(instanceInfo.getTaskTrackerAddress())) {
            log.warn("[InstanceManager-{}] TaskTrackerAddress is empty, server will wait then acquire again!", instanceId);
            CommonUtils.easySleep(277);
            instanceInfo = instanceInfoRepository.findByInstanceId(instanceId);
        }

        int originStatus = instanceInfo.getStatus();
        // 丢弃过期的上报数据
        if (req.getReportTime() <= instanceInfo.getLastReportTime()) {
            log.warn("[InstanceManager-{}] receive the expired status report request: {}, this report will be dropped.", instanceId, req);
            return;
        }
        // 丢弃非目标 TaskTracker 的上报数据（脑裂情况）
        if (!req.getSourceAddress().equals(instanceInfo.getTaskTrackerAddress())) {
            log.warn("[InstanceManager-{}] receive the other TaskTracker's report: {}, but current TaskTracker is {}, this report will be dropped.", instanceId, req, instanceInfo.getTaskTrackerAddress());
            return;
        }

        InstanceStatus receivedInstanceStatus = InstanceStatus.of(req.getInstanceStatus());
        Integer timeExpressionType = jobInfo.getTimeExpressionType();
        // 更新 最后上报时间 和 修改时间
        instanceInfo.setLastReportTime(req.getReportTime());
        instanceInfo.setUpdateTime(LocalDateTime.now());

        // FREQUENT 任务没有失败重试机制，TaskTracker一直运行即可，只需要将存活信息同步到DB即可
        // FREQUENT 任务的 newStatus 只有2中情况，一种是 RUNNING，一种是 FAILED（表示该机器 overload，需要重新选一台机器执行）
        // 综上，直接把 status 和 runningNum 同步到DB即可
        if (TimeExpressionType.FREQUENT_TYPES.contains(timeExpressionType)) {
            // 如果实例处于失败状态，则说明该 worker 失联了一段时间，被 server 判定为宕机，而此时该秒级任务有可能已经重新派发了，故需要 Kill 掉该实例
            // fix issue 375
            if (instanceInfo.getStatus() == InstanceStatus.FAILED.getV()) {
                log.warn("[InstanceManager-{}] receive TaskTracker's report: {}, but current instance is already failed, this instance should be killed.", instanceId, req);
                stopInstance(instanceId, instanceInfo);
                return;
            }
            LifeCycle lifeCycle = LifeCycle.parse(jobInfo.getLifecycle());
            // 检查生命周期是否已结束
            if (lifeCycle.getEnd() != null && lifeCycle.getEnd() <= System.currentTimeMillis()) {
                stopInstance(instanceId, instanceInfo);
                instanceInfo.setStatus(InstanceStatus.SUCCEED.getV());
            } else {
                instanceInfo.setStatus(receivedInstanceStatus.getV());
            }
            instanceInfo.setResult(req.getResult());
            instanceInfo.setRunningTimes(req.getTotalTaskNum());
            instanceInfoRepository.saveOrUpdate(instanceInfo);
            // 任务需要告警
            if (req.isNeedAlert()) {
                log.info("[InstanceManager-{}] receive frequent task alert req,time:{},content:{}", instanceId, req.getReportTime(), req.getAlertContent());
                alert(instanceId, req.getAlertContent());
            }
            return;
        }
        // 更新运行次数
        if (instanceInfo.getStatus() == InstanceStatus.WAITING_WORKER_RECEIVE.getV()) {
            // 这里不会存在并发问题
            instanceInfo.setRunningTimes(instanceInfo.getRunningTimes() + 1);
        }
        // QAQ ，不能提前变更 status，否则会导致更新运行次数的逻辑不生效继而导致普通任务 无限重试
        instanceInfo.setStatus(receivedInstanceStatus.getV());

        boolean finished = false;
        if (receivedInstanceStatus == InstanceStatus.SUCCEED) {
            instanceInfo.setResult(req.getResult());
            instanceInfo.setFinishedTime(LocalDateTimeUtil.of(req.getEndTime() == null ? System.currentTimeMillis() : req.getEndTime()));
            finished = true;
        } else if (receivedInstanceStatus == InstanceStatus.FAILED) {

            // 当前重试次数 <= 最大重试次数，进行重试 （第一次运行，runningTimes为1，重试一次，instanceRetryNum也为1，故需要 =）
            if (instanceInfo.getRunningTimes() <= jobInfo.getInstanceRetryNum()) {

                log.info("[InstanceManager-{}] instance execute failed but will take the {}th retry.", instanceId, instanceInfo.getRunningTimes());

                // 延迟10S重试（由于重试不改变 instanceId，如果派发到同一台机器，上一个 TaskTracker 还处于资源释放阶段，无法创建新的TaskTracker，任务失败）
                instanceInfo.setExpectedTriggerTime(System.currentTimeMillis() + 10000);

                // 修改状态为 等待派发，正式开始重试
                // 问题：会丢失以往的调度记录（actualTriggerTime什么的都会被覆盖）
                instanceInfo.setStatus(InstanceStatus.WAITING_DISPATCH.getV());
            } else {
                instanceInfo.setResult(req.getResult());
                instanceInfo.setFinishedTime(LocalDateTimeUtil.of(req.getEndTime() == null ? System.currentTimeMillis() : req.getEndTime()));
                finished = true;
                log.info("[InstanceManager-{}] instance execute failed and have no chance to retry.", instanceId);
            }
        }
        if (finished) {
            // 最终状态允许直接覆盖更新
            instanceInfoRepository.saveOrUpdate(instanceInfo);
            // 这里的 InstanceStatus 只有 成功/失败 两种，手动停止不会由 TaskTracker 上报
            processFinishedInstance(instanceId, IdConvertUtil.toString(req.getWfInstanceId()), receivedInstanceStatus, req.getResult());
            return;
        }
        // 带条件更新
        final int i = instanceInfoRepository.updateStatusChangeInfoByInstanceIdAndStatus(instanceInfo.getLastReportTime(), instanceInfo.getRunningTimes(), instanceInfo.getStatus(), instanceInfo.getInstanceId(), originStatus);
        if (i == 0) {
            log.warn("[InstanceManager-{}] update instance status failed, maybe the instance status has been changed by other thread. discard this status change,{}", instanceId, instanceInfo);
        }
    }

    private void stopInstance(String instanceId, InstanceInfoDO instanceInfo) {
        Optional<WorkerInfo> workerInfoOpt = workerClusterQueryService.getWorkerInfoByAddress(instanceInfo.getAppId(), instanceInfo.getTaskTrackerAddress());
        if (workerInfoOpt.isPresent()) {
            ServerStopInstanceReq stopInstanceReq = new ServerStopInstanceReq(Long.parseLong(instanceId));
            WorkerInfo workerInfo = workerInfoOpt.get();
            final URL url = ServerURLFactory.stopInstance2Worker(workerInfo.getAddress());
            transportService.tell(workerInfo.getProtocol(), url, stopInstanceReq);
        }
    }

    /**
     * 收尾完成的任务实例
     *
     * @param instanceId   任务实例ID
     * @param wfInstanceId 工作流实例ID，非必须
     * @param status       任务状态，有 成功/失败/手动停止
     * @param result       执行结果
     */
    public void processFinishedInstance(String instanceId, String wfInstanceId, InstanceStatus status, String result) {

        log.info("[Instance-{}] process finished, final status is {}.", instanceId, status.name());

        // 上报日志数据
        HashedWheelTimerHolder.INACCURATE_TIMER.schedule(() -> instanceLogService.sync(instanceId), 60, TimeUnit.SECONDS);

        // workflow 特殊处理
        if (wfInstanceId != null) {
            // 手动停止在工作流中也认为是失败（理论上不应该发生）
            workflowInstanceManager.move(wfInstanceId, instanceId, status, result);
        }

        // 告警
        if (status == InstanceStatus.FAILED) {
            alert(instanceId, result);
        }
        // 主动移除缓存，减小内存占用
        instanceMetadataService.invalidateJobInfo(instanceId);
    }

    private void alert(String instanceId, String alertContent) {
        InstanceInfoDO instanceInfo = instanceInfoRepository.findByInstanceId(instanceId);
        JobInfoDO jobInfo;
        try {
            jobInfo = instanceMetadataService.fetchJobInfoByInstanceId(instanceId);
        } catch (Exception e) {
            log.warn("[InstanceManager-{}] can't find jobInfo, alarm failed.", instanceId);
            return;
        }
        JobInstanceAlarm content = new JobInstanceAlarm();
        BeanUtil.copyProperties(jobInfo, content);
        BeanUtil.copyProperties(instanceInfo, content);
        List<AnUser> userList = AnnoBeanUtils.getBean(AnUserDao.class).selectUserList(jobInfo.getNotifyUserIds());
        if (!StringUtils.isEmpty(alertContent)) {
            content.setResult(alertContent);
        }
        alarmCenter.alarmFailed(content, AlarmUtils.convertUserInfoList2AlarmTargetList(userList));
    }

    @Override
    public void setTransportService(TransportService transportService) {
        this.transportService = transportService;
    }
}
