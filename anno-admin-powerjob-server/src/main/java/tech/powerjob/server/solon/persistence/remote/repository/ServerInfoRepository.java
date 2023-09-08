package tech.powerjob.server.solon.persistence.remote.repository;

import org.noear.wood.xml.Namespace;
import tech.powerjob.server.solon.persistence.remote.model.ServerInfoDO;
import site.sorghum.anno.suppose.mapper.AnnoBaseMapper;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 服务器信息 数据操作层
 *
 * @author tjq
 * @since 2020/4/15
 */
@Namespace("powerjob.server")
public interface ServerInfoRepository extends AnnoBaseMapper<ServerInfoDO> {


    default ServerInfoDO findByIp(String ip) {
        return selectItem(m -> m.whereEq("ip", ip));
    }

    default int updateGmtModifiedByIp(String ip) {
        ServerInfoDO serverInfoDO = new ServerInfoDO();
        serverInfoDO.setUpdateTime(LocalDateTime.now());
        return update(serverInfoDO, m -> m.whereEq("ip", ip));
    }

    default int updateIdByIp(String id, String ip) {
        ServerInfoDO serverInfoDO = new ServerInfoDO();
        serverInfoDO.setId(id);
        return update(serverInfoDO, m -> m.whereEq("ip", ip));
    }

    default int deleteByGmtModifiedBefore(Date threshold) {
        return delete(m -> m.whereLt("update_time", threshold));
    }
}
