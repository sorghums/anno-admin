package tech.powerjob.server.solon.persistence.local;

import org.noear.wood.xml.Namespace;
import site.sorghum.anno.pre.suppose.mapper.AnnoBaseMapper;

import java.util.List;

/**
 * 本地运行时日志数据操作层
 *
 * @author tjq
 * @since 2020/4/27
 */
@Namespace("site.sorghum.anno.powerjob.server.persistence.local")
public interface LocalInstanceLogRepository extends AnnoBaseMapper<LocalInstanceLogDO> {

    /**
     * 流式查询
     */
    default List<LocalInstanceLogDO> findByInstanceIdOrderByLogTime(String instanceId) {
        return selectList(m -> m.whereEq("instance_id", instanceId).orderByAsc("log_time"));
    }

    /**
     * 删除数据
     */
    default long deleteByInstanceId(String instanceId) {
        return delete(m -> m.whereEq("instance_id", instanceId));
    }

    default long deleteByInstanceIdInAndLogTimeLessThan(List<String> instanceIds, Long t) {
        return delete(m -> m.whereIn("instance_id", instanceIds).andLt("log_time", t));
    }

    default long countByInstanceId(String instanceId) {
        return selectCount(m -> m.whereEq("instance_id", instanceId));
    }
}
