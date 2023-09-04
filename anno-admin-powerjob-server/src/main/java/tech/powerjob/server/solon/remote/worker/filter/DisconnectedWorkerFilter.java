package tech.powerjob.server.solon.remote.worker.filter;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import tech.powerjob.server.solon.common.module.WorkerInfo;
import tech.powerjob.server.solon.persistence.remote.model.JobInfoDO;

/**
 * filter disconnected worker
 *
 * @author tjq
 * @since 2021/2/19
 */
@Slf4j
@Component
public class DisconnectedWorkerFilter implements WorkerFilter {

    @Override
    public boolean filter(WorkerInfo workerInfo, JobInfoDO jobInfo) {
        boolean timeout = workerInfo.timeout();
        if (timeout) {
            log.info("[Job-{}] filter worker[{}] due to timeout(lastActiveTime={})", jobInfo.getId(), workerInfo.getAddress(), workerInfo.getLastActiveTime());
        }
        return timeout;
    }
}
