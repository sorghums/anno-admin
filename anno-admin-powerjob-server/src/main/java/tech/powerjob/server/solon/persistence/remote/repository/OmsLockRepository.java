package tech.powerjob.server.solon.persistence.remote.repository;

import org.noear.wood.xml.Namespace;
import site.sorghum.anno.suppose.mapper.AnnoBaseMapper;
import tech.powerjob.server.solon.persistence.remote.model.OmsLockDO;

/**
 * 利用唯一性约束作为数据库锁
 *
 * @author tjq
 * @since 2020/4/2
 */
@Namespace("powerjob.server")
public interface OmsLockRepository extends AnnoBaseMapper<OmsLockDO> {

    default int deleteByLockName(String lockName) {
        return delete(m -> m.whereEq("lock_name", lockName));
    }

    default OmsLockDO findByLockName(String lockName) {
        return selectItem(m -> m.whereEq("lock_name", lockName));
    }

    default int deleteByOwnerIP(String ip) {
        return delete(m -> m.whereEq("owner_ip", ip));
    }
}
