package tech.powerjob.server.solon.remote.worker.filter;

import tech.powerjob.server.solon.common.module.WorkerInfo;
import tech.powerjob.server.solon.persistence.remote.model.JobInfoDO;

/**
 * filter worker by system metrics or other info
 *
 * @author tjq
 * @since 2021/2/16
 */
public interface WorkerFilter {

    /**
     * @param workerInfo worker info, maybe you need to use your customized info in SystemMetrics#extra
     * @param jobInfoDO  job info
     * @return true will remove the worker in process list
     */
    boolean filter(WorkerInfo workerInfo, JobInfoDO jobInfoDO);
}
