package tech.powerjob.server.solon.persistence.remote.repository;

import site.sorghum.anno.suppose.mapper.AnnoBaseMapper;
import tech.powerjob.server.solon.persistence.remote.model.WorkflowNodeInfoDO;

import java.util.Date;
import java.util.List;


/**
 * WorkflowNodeInfo 数据访问层
 *
 * @author Echo009
 * @since 2021/2/1
 */
public interface WorkflowNodeInfoRepository extends AnnoBaseMapper<WorkflowNodeInfoDO> {

    /**
     * 根据工作流id查找所有的节点
     *
     * @param workflowId 工作流id
     * @return 节点信息集合
     */
    default List<WorkflowNodeInfoDO> findByWorkflowId(String workflowId) {
        return selectList(m -> m.whereEq("workflow_id", workflowId));
    }

    /**
     * 根据工作流节点 ID 删除节点
     *
     * @param workflowId 工作流ID
     * @param id         节点 ID
     * @return 删除记录数
     */
    default int deleteByWorkflowIdAndIdNotIn(String workflowId, List<String> id) {
        return delete(m -> m.whereEq("workflow_id", workflowId).andNin("id", id));
    }

    /**
     * 删除工作流 ID 为空，且创建时间早于指定时间的节点信息
     *
     * @param crtTimeThreshold 创建时间阈值
     * @return 删除记录条数
     */
    default int deleteAllByWorkflowIdIsNullAndGmtCreateBefore(Date crtTimeThreshold) {
        return delete(m -> m.whereEq("workflow_id", null).andLt("update_time", crtTimeThreshold));
    }

}
