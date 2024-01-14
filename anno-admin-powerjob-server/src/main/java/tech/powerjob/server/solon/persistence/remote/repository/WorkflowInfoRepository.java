package tech.powerjob.server.solon.persistence.remote.repository;

import org.noear.wood.IPage;
import org.noear.wood.xml.Namespace;
import site.sorghum.anno.db.DbPage;
import site.sorghum.anno.suppose.mapper.AnnoBaseMapper;
import tech.powerjob.server.solon.persistence.remote.model.WorkflowInfoDO;

import java.util.List;

/**
 * DAG 工作流 数据操作层
 *
 * @author tjq
 * @since 2020/5/26
 */
@Namespace("powerjob.server")
public interface WorkflowInfoRepository extends AnnoBaseMapper<WorkflowInfoDO> {


    default List<WorkflowInfoDO> findByAppIdInAndStatusAndTimeExpressionTypeAndNextTriggerTimeLessThanEqual(List<String> appIds, int status, int timeExpressionType, long time) {
        return selectList(m -> m.whereIn("app_id", appIds)
                .andEq("status", status)
                .andEq("time_expression_type", timeExpressionType)
                .andLte("next_trigger_time", time));
    }

    /**
     * 查询指定 APP 下所有的工作流信息
     *
     * @param appId APP ID
     * @return 该 APP 下的所有工作流信息
     */
    default List<WorkflowInfoDO> findByAppId(String appId) {
        return selectList(m -> m.whereEq("app_id", appId));
    }

    /**
     * 对外查询（list）三兄弟
     */
    default IPage<WorkflowInfoDO> findByAppIdAndStatusNot(Long appId, int nStatus, DbPage page) {
        return selectPage(page.getOffset(), page.getPageSize(),
            m -> m.whereEq("app_id", appId).andNeq("status", nStatus));
    }

    default IPage<WorkflowInfoDO> findByIdAndStatusNot(String id, int nStatus, DbPage page) {
        return selectPage(page.getOffset(), page.getPageSize(),
            m -> m.whereEq("id", id).andNeq("status", nStatus));
    }

    default IPage<WorkflowInfoDO> findByAppIdAndStatusNotAndWfNameLike(String appId, int nStatus, String condition, DbPage page) {
        return selectPage(page.getOffset(), page.getPageSize(),
            m -> m.whereEq("app_id", appId).andNeq("status", nStatus).andLk("wf_name", condition));
    }
}
