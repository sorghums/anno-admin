package tech.powerjob.server.solon.remote.server.self;

import cn.hutool.core.collection.CollUtil;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.bean.LifecycleBean;
import org.noear.solon.scheduling.annotation.Scheduled;
import org.noear.wood.annotation.Db;
import tech.powerjob.server.solon.common.module.ServerInfo;
import tech.powerjob.server.solon.extension.LockService;
import tech.powerjob.server.solon.persistence.remote.model.ServerInfoDO;
import tech.powerjob.server.solon.persistence.remote.repository.ServerInfoRepository;
import tech.powerjob.common.exception.PowerJobException;
import tech.powerjob.common.utils.CommonUtils;
import tech.powerjob.common.utils.NetUtils;

import java.util.Date;
import java.util.List;

/**
 * management server info, like heartbeat, server id etc
 *
 * @author tjq
 * @since 2021/2/21
 */
@Slf4j
@Component
public class ServerInfoServiceImpl implements ServerInfoService, LifecycleBean {

    private ServerInfo serverInfo;

    @Db
    private ServerInfoRepository serverInfoRepository;
    @Inject
    private LockService lockService;

    private static final long MAX_SERVER_CLUSTER_SIZE = 10000;

    private static final String SERVER_INIT_LOCK = "server_init_lock";
    private static final int SERVER_INIT_LOCK_MAX_TIME = 15000;


    @Override
    public void start() {

        this.serverInfo = new ServerInfo();

        String ip = NetUtils.getLocalHost();
        serverInfo.setIp(ip);
        serverInfo.setBornTime(System.currentTimeMillis());
        serverInfo.setVersion(Solon.cfg().get("powerjob-server-solon.version", "UNKNOWN"));

        Stopwatch sw = Stopwatch.createStarted();

        while (!lockService.tryLock(SERVER_INIT_LOCK, SERVER_INIT_LOCK_MAX_TIME)) {
            log.info("[ServerInfoService] waiting for lock: {}", SERVER_INIT_LOCK);
            CommonUtils.easySleep(100);
        }

        try {

            // register server then get server_id
            ServerInfoDO server = serverInfoRepository.findByIp(ip);
            List<ServerInfoDO> infos = serverInfoRepository.selectTop(1, m -> m.orderByDesc("id"));
            long maxId = CollUtil.isEmpty(infos) ? 0 : Long.parseLong(infos.get(0).getId());
            if (server == null) {
                ServerInfoDO newServerInfo = new ServerInfoDO(ip);
                newServerInfo.setId(String.valueOf(maxId + 1));
                serverInfoRepository.insert(newServerInfo);
                server = newServerInfo;
            } else {
                serverInfoRepository.updateGmtModifiedByIp(ip);
            }

            if (maxId + 1 >= MAX_SERVER_CLUSTER_SIZE) {
                long newId = retryServerId();
                serverInfo.setId(newId);
                serverInfoRepository.updateIdByIp(String.valueOf(newId), ip);
            } else {
                serverInfo.setId(Long.parseLong(server.getId()));
            }
        } catch (Exception e) {
            log.error("[ServerInfoService] init server failed", e);
            throw e;
        } finally {
            lockService.unlock(SERVER_INIT_LOCK);
        }

        log.info("[ServerInfoService] ip:{}, id:{}, cost:{}", ip, serverInfo.getId(), sw);
    }

    @Scheduled(fixedRate = 15000, initialDelay = 15000)
    public void heartbeat() {
        serverInfoRepository.updateGmtModifiedByIp(serverInfo.getIp());
    }


    /**
     * 清理无效的 server 记录
     */
    private long retryServerId() {
        List<Object> ids = serverInfoRepository.selectArray("id", null);

        log.info("[ServerInfoService] current server record num in database: {}", ids.size());

        if (ids.size() > MAX_SERVER_CLUSTER_SIZE) {
            // use a large time interval to prevent valid records from being deleted when the local time is inaccurate
            Date oneDayAgo = DateUtils.addDays(new Date(), -1);
            int delNum = serverInfoRepository.deleteByGmtModifiedBefore(oneDayAgo);
            log.warn("[ServerInfoService] delete invalid {} server info record before {}", delNum, oneDayAgo);

            ids = serverInfoRepository.selectArray("id", null);
        }

        if (ids.size() > MAX_SERVER_CLUSTER_SIZE) {
            throw new PowerJobException(String.format("The powerjob-server cluster cannot accommodate %d machines, please rebuild another cluster", ids.size()));
        }

        for (long i = 1; i <= MAX_SERVER_CLUSTER_SIZE; i++) {
            if (ids.contains(String.valueOf(i))) {
                continue;
            }

            log.info("[ServerInfoService] ID[{}] is not used yet, try as new server id", i);
            return i;
        }
        throw new PowerJobException("impossible");
    }

    @Override
    public ServerInfo fetchServiceInfo() {
        if (serverInfo == null) {
            start();
        }
        return serverInfo;
    }

}
