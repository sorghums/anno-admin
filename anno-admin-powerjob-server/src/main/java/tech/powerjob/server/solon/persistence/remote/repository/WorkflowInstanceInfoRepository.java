package tech.powerjob.server.solon.persistence.remote.repository;

import tech.powerjob.server.solon.persistence.remote.model.WorkflowInstanceInfoDO;
import site.sorghum.anno.suppose.mapper.AnnoBaseMapper;

import java.util.Date;
import java.util.List;

/**
 * 工作流运行实例数据操作
 *
 * @author tjq
 * @since 2020/5/26
 */
public interface WorkflowInstanceInfoRepository extends AnnoBaseMapper<WorkflowInstanceInfoDO> {

    /**
     * 查找对应工作流实例
     *
     * @param wfInstanceId 实例 ID
     * @return 工作流实例
     */
    default WorkflowInstanceInfoDO findByWfInstanceId(String wfInstanceId) {
        return selectItem(m -> m.whereEq("wf_instance_id", wfInstanceId));
    }

    /**
     * 删除历史数据，JPA自带的删除居然是根据ID循环删，2000条数据删了几秒，也太拉垮了吧...
     * 结果只能用 int 接收
     *
     * @param time   更新时间阈值
     * @param status 状态列表
     * @return 删除的记录条数
     */
    default int deleteAllByGmtModifiedBeforeAndStatusIn(Date time, List<Integer> status) {
        return delete(m -> m.whereLt("update_time", time).andIn("status", status));
    }

    /**
     * 统计该工作流下处于对应状态的实例数量
     *
     * @param workflowId 工作流 ID
     * @param status     状态列表
     */
    default Long countByWorkflowIdAndStatusIn(String workflowId, List<Integer> status) {
        return selectCount(m -> m.whereEq("workflow_id", workflowId).andIn("status", status));
    }

    /**
     * 加载期望调度时间小于给定阈值的
     *
     * @param appIds 应用 ID 列表
     * @param status 状态
     * @param time   期望调度时间阈值
     * @return 工作流实例列表
     */
    default List<WorkflowInstanceInfoDO> findByAppIdInAndStatusAndExpectedTriggerTimeLessThan(List<String> appIds, int status, long time) {
        return selectList(m -> m.whereIn("app_id", appIds).andEq("status", status).andLt("expected_trigger_time", time));
    }
}
