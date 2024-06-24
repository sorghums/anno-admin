package tech.powerjob.server.solon.remote.worker.filter;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import tech.powerjob.common.model.SystemMetrics;
import tech.powerjob.server.solon.common.module.WorkerInfo;
import tech.powerjob.server.solon.persistence.remote.model.JobInfoDO;

/**
 * filter worker by system metric
 *
 * @author tjq
 * @since 2021/2/19
 */
@Slf4j
@Component
public class SystemMetricsWorkerFilter implements WorkerFilter {

    @Override
    public boolean filter(WorkerInfo workerInfo, JobInfoDO jobInfo) {
        SystemMetrics metrics = workerInfo.getSystemMetrics();
        boolean filter = !metrics.available(jobInfo.getMinCpuCores(), jobInfo.getMinMemorySpace(), jobInfo.getMinDiskSpace());
        if (filter) {
            log.info("[Job-{}] filter worker[{}] because the {} do not meet the requirements", jobInfo.getId(), workerInfo.getAddress(), workerInfo.getSystemMetrics());
        }
        return filter;
    }
}
