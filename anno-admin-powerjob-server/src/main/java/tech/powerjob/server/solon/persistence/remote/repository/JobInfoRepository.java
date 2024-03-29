package tech.powerjob.server.solon.persistence.remote.repository;

import org.noear.wood.IPage;
import org.noear.wood.xml.Namespace;
import site.sorghum.anno.db.DbPage;
import site.sorghum.anno.suppose.mapper.AnnoBaseMapper;
import tech.powerjob.server.solon.persistence.remote.model.JobInfoDO;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JobInfo 数据访问层
 *
 * @author tjq
 * @since 2020/4/1
 */
@Namespace("powerjob.server")
public interface JobInfoRepository extends AnnoBaseMapper<JobInfoDO> {


    /**
     * 调度专用
     */
    default List<JobInfoDO> findByAppIdInAndStatusAndTimeExpressionTypeAndNextTriggerTimeLessThanEqual(List<String> appIds, int status, int timeExpressionType, long time) {
        return selectList(m -> m.whereIn("app_id", appIds)
            .andEq("status", status)
            .andEq("time_expression_type", timeExpressionType)
            .andLte("next_trigger_time", time));
    }

    default List<String> findByAppIdInAndStatusAndTimeExpressionTypeIn(List<String> appIds, int status, List<Integer> timeTypes) {
        List<Object> objects = selectArray("id", m -> m.whereIn("app_id", appIds)
            .andEq("status", status)
            .andIn("time_expression_type", timeTypes));
        return objects.stream().map(String::valueOf).collect(Collectors.toList());
    }

    default IPage<JobInfoDO> findByAppIdAndStatusNot(String appId, int status, DbPage page) {
        return selectPage(page.getOffset(), page.getPageSize(), m -> m.whereEq("app_id", appId).andNeq("status", status));
    }

    default IPage<JobInfoDO> findByAppIdAndJobNameLikeAndStatusNot(String appId, String condition, int status, DbPage page) {
        return selectPage(page.getOffset(), page.getPageSize(), m ->
            m.whereEq("app_id", appId)
                .andLk("job_name", condition)
                .andNeq("status", status));
    }

    /**
     * 校验工作流包含的任务
     *
     * @param appId     APP ID
     * @param statusSet 状态列表
     * @param jobIds    任务ID
     * @return 数量
     */
    default long countByAppIdAndStatusInAndIdIn(String appId, Set<Integer> statusSet, Set<String> jobIds) {
        return selectCount(m -> m.whereEq("app_id", appId)
            .andIn("status", statusSet)
            .andIn("id", jobIds));
    }

    default long countByAppIdAndStatusNot(String appId, int status) {
        return selectCount(m -> m.whereEq("app_id", appId).andNeq("status", status));
    }

    default List<JobInfoDO> findByAppId(String appId) {
        return selectList(m -> m.whereEq("app_id", appId));
    }

    default List<JobInfoDO> findByIdIn(Collection<String> jobIds) {
        return selectByIds(jobIds);
    }

}
