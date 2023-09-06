package tech.powerjob.server.solon.persistence.remote.repository;

import org.noear.wood.IPage;
import org.noear.wood.xml.Namespace;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.db.param.PageParam;
import tech.powerjob.server.solon.persistence.remote.model.InstanceInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.brief.BriefInstanceInfo;
import site.sorghum.anno.pre.suppose.mapper.AnnoBaseMapper;
import tech.powerjob.common.enums.InstanceStatus;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JobLog 数据访问层
 *
 * @author tjq
 * @since 2020/4/1
 */
@Namespace("powerjob.server")
public interface InstanceInfoRepository extends AnnoBaseMapper<InstanceInfoDO> {

    /**
     * 统计当前JOB有多少实例正在运行
     */
    default long countByJobIdAndStatusIn(String jobId, List<Integer> status) {
        return selectCount(m -> m.whereEq("job_id", jobId).andIn("status", status));
    }

    default List<InstanceInfoDO> findByJobIdAndStatusIn(String jobId, List<Integer> status) {
        return selectList(m -> m.whereEq("job_id", jobId).andIn("status", status));
    }

    /**
     * 更新状态变更信息
     *
     * @param lastReportTime 最近一次上报时间
     * @param runningTimes   运行次数
     * @param instanceId     实例 ID
     * @param status         目标状态
     * @param oldStatus      旧状态
     * @return 更新记录数
     */
    default int updateStatusChangeInfoByInstanceIdAndStatus(long lastReportTime, long runningTimes, int status, String instanceId, int oldStatus) {
        InstanceInfoDO instanceInfo = new InstanceInfoDO();
        instanceInfo.setLastReportTime(lastReportTime);
        instanceInfo.setUpdateTime(LocalDateTime.now());
        instanceInfo.setRunningTimes(runningTimes);
        instanceInfo.setStatus(status);
        return update(instanceInfo, m -> m.whereEq("instance_id", instanceId).andEq("status", oldStatus));
    }

    /**
     * 更新任务执行记录内容（DispatchService专用）
     *
     * @param instanceId         实例 ID
     * @param actualTriggerTime  实际调度时间
     * @param finishedTime       完成时间
     * @param taskTrackerAddress taskTracker 地址
     * @param result             结果
     * @return 更新记录数量
     */
    default int update4TriggerFailed(String instanceId, long actualTriggerTime, long finishedTime, String taskTrackerAddress, String result) {
        InstanceInfoDO instanceInfo = new InstanceInfoDO();
        instanceInfo.setStatus(InstanceStatus.FAILED.getV());
        instanceInfo.setActualTriggerTime(actualTriggerTime);
        instanceInfo.setFinishedTime(finishedTime);
        instanceInfo.setTaskTrackerAddress(taskTrackerAddress);
        instanceInfo.setResult(result);
        instanceInfo.setUpdateTime(LocalDateTime.now());
        return update(instanceInfo, m -> m.whereEq("instance_id", instanceId));
    }


    /**
     * 更新任务执行记录内容（DispatchService专用）
     *
     * @param instanceId         任务实例ID，分布式唯一
     * @param actualTriggerTime  实际调度时间
     * @param taskTrackerAddress taskTracker 地址
     * @param oldStatus          旧状态
     * @return 更新记录数量
     */
    default int update4TriggerSucceed(String instanceId, long actualTriggerTime, String taskTrackerAddress, int oldStatus) {
        InstanceInfoDO instanceInfo = new InstanceInfoDO();
        instanceInfo.setStatus(InstanceStatus.WAITING_WORKER_RECEIVE.getV());
        instanceInfo.setActualTriggerTime(actualTriggerTime);
        instanceInfo.setTaskTrackerAddress(taskTrackerAddress);
        instanceInfo.setUpdateTime(LocalDateTime.now());
        return update(instanceInfo, m -> m.whereEq("instance_id", instanceId).andEq("status", oldStatus));
    }


    default int updateStatusAndGmtModifiedByInstanceIdAndOriginStatus(String instanceId, int originStatus, int status) {
        InstanceInfoDO instanceInfo = new InstanceInfoDO();
        instanceInfo.setStatus(status);
        instanceInfo.setUpdateTime(LocalDateTime.now());
        return update(instanceInfo, m -> m.whereEq("instance_id", instanceId).andEq("status", originStatus));
    }

    default int updateStatusAndGmtModifiedByInstanceIdListAndOriginStatus(List<String> instanceIdList, int originStatus, int status) {
        InstanceInfoDO instanceInfo = new InstanceInfoDO();
        instanceInfo.setStatus(status);
        instanceInfo.setUpdateTime(LocalDateTime.now());
        return update(instanceInfo, m -> m.whereIn("instance_id", instanceIdList).andEq("status", originStatus));
    }

    /**
     * 更新固定频率任务的执行记录
     *
     * @param instanceId   任务实例ID，分布式唯一
     * @param status       状态
     * @param runningTimes 执行次数
     * @return 更新记录数量
     */
    default int update4FrequentJob(long instanceId, int status, long runningTimes) {
        InstanceInfoDO instanceInfo = new InstanceInfoDO();
        instanceInfo.setStatus(status);
        instanceInfo.setRunningTimes(runningTimes);
        instanceInfo.setUpdateTime(LocalDateTime.now());
        return update(instanceInfo, m -> m.whereEq("instance_id", instanceId));
    }

    default List<InstanceInfoDO> findAllByAppIdInAndStatusAndExpectedTriggerTimeLessThan(List<String> appIds, int status, long time, PageParam page) {
        IPage<InstanceInfoDO> selectedPage = selectPage(page.getPage(), page.getPageSize(),
            m -> m.whereIn("app_id", appIds).andEq("status", status).andLt("expected_trigger_time", time));
        return selectedPage.getList();
    }

    default List<BriefInstanceInfo> selectBriefInfoByAppIdInAndStatusAndActualTriggerTimeLessThan(List<String> appIds, int status, long time, PageParam page) {
        try {
            return db().table("pj_instance_info")
                .whereIn("app_id", appIds)
                .andEq("status", status)
                .andLt("actual_trigger_time", time)
                .limit(page.getOffset(), page.getPageSize())
                .selectList("app_id,id,job_id,instance_id", BriefInstanceInfo.class);
        } catch (SQLException e) {
            throw new BizException(e);
        }
    }

    default List<BriefInstanceInfo> selectBriefInfoByAppIdInAndStatusAndGmtModifiedBefore(List<String> appIds, int status, Date time, PageParam page) {
        try {
            return db().table("pj_instance_info")
                .whereIn("app_id", appIds)
                .andEq("status", status)
                .andLt("update_time", time)
                .limit(page.getOffset(), page.getPageSize())
                .selectList("app_id,id,job_id,instance_id", BriefInstanceInfo.class);
        } catch (SQLException e) {
            throw new BizException(e);
        }
    }


    default InstanceInfoDO findByInstanceId(String instanceId) {
        return selectItem(m -> m.whereEq("instance_id", instanceId));
    }

    /* --数据统计-- */

    default long countByAppIdAndStatus(String appId, int status) {
        return selectCount(m -> m.whereEq("app_id", appId).andEq("status", status));
    }

    default long countByAppIdAndStatusAndGmtCreateAfter(String appId, int status, Date time) {
        return selectCount(m -> m.whereEq("app_id", appId).andEq("status", status).andGt("create_time", time));
    }

    default List<String> findByJobIdInAndStatusIn(List<String> jobIds, List<Integer> status) {
        List<Object> objects = selectArray("distinct job_id", m -> m.whereIn("job_id", jobIds).andIn("status", status));
        return objects.stream().map(String::valueOf).collect(Collectors.toList());
    }

    /**
     * 删除历史数据，JPA自带的删除居然是根据ID循环删，2000条数据删了几秒，也太拉垮了吧...
     * 结果只能用 int 接收
     *
     * @param time   更新时间阈值
     * @param status 状态
     * @return 删除记录数
     */
    default int deleteAllByGmtModifiedBeforeAndStatusIn(Date time, List<Integer> status) {
        return delete(m -> m.whereLt("update_time", time).andIn("status", status));
    }
}
