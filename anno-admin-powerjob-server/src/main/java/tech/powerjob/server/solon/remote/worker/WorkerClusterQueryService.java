package tech.powerjob.server.solon.remote.worker;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import tech.powerjob.server.solon.common.module.WorkerInfo;
import tech.powerjob.server.solon.persistence.remote.model.JobInfoDO;
import tech.powerjob.server.solon.remote.server.redirector.DesignateServer;
import tech.powerjob.server.solon.remote.worker.filter.WorkerFilter;
import tech.powerjob.common.enums.DispatchStrategy;
import tech.powerjob.common.model.DeployedContainerInfo;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 获取 worker 集群信息
 *
 * @author tjq
 * @since 2021/2/19
 */
@Slf4j
@Component
public class WorkerClusterQueryService {

    @Inject
    private List<WorkerFilter> workerFilters;

    /**
     * get worker for job
     *
     * @param jobInfo job
     * @return worker cluster info, sorted by metrics desc
     */
    public List<WorkerInfo> getSuitableWorkers(JobInfoDO jobInfo) {

        List<WorkerInfo> workers = Lists.newLinkedList(getWorkerInfosByAppId(jobInfo.getAppId()).values());

        workers.removeIf(workerInfo -> filterWorker(workerInfo, jobInfo));

        DispatchStrategy dispatchStrategy = DispatchStrategy.of(jobInfo.getDispatchStrategy());
        switch (dispatchStrategy) {
            case RANDOM:
                Collections.shuffle(workers);
                break;
            case HEALTH_FIRST:
                workers.sort((o1, o2) -> o2.getSystemMetrics().calculateScore() - o1.getSystemMetrics().calculateScore());
                break;
            default:
                // do nothing
        }

        // 限定集群大小（0代表不限制）
        if (!workers.isEmpty() && jobInfo.getMaxWorkerCount() > 0 && workers.size() > jobInfo.getMaxWorkerCount()) {
            workers = workers.subList(0, jobInfo.getMaxWorkerCount());
        }
        return workers;
    }

    @DesignateServer
    public List<WorkerInfo> getAllWorkers(String appId) {
        List<WorkerInfo> workers = Lists.newLinkedList(getWorkerInfosByAppId(appId).values());
        workers.sort((o1, o2) -> o2.getSystemMetrics().calculateScore() - o1.getSystemMetrics().calculateScore());
        return workers;
    }

    /**
     * get all alive workers
     *
     * @param appId appId
     * @return alive workers
     */
    @DesignateServer
    public List<WorkerInfo> getAllAliveWorkers(String appId) {
        List<WorkerInfo> workers = Lists.newLinkedList(getWorkerInfosByAppId(appId).values());
        workers.removeIf(WorkerInfo::timeout);
        return workers;
    }

    /**
     * Gets worker info by address.
     *
     * @param appId   the app id
     * @param address the address
     * @return the worker info by address
     */
    public Optional<WorkerInfo> getWorkerInfoByAddress(String appId, String address) {
        // this may cause NPE while address value is null .
        final Map<String, WorkerInfo> workerInfosByAppId = getWorkerInfosByAppId(appId);
        //add null check for both workerInfos Map and  address
        if (null != workerInfosByAppId && null != address) {
            return Optional.ofNullable(workerInfosByAppId.get(address));
        }
        return Optional.empty();
    }

    public Map<String, ClusterStatusHolder> getAppId2ClusterStatus() {
        return WorkerClusterManagerService.getAppId2ClusterStatus();
    }

    /**
     * 获取某个应用容器的部署情况
     *
     * @param appId       应用ID
     * @param containerId 容器ID
     * @return 部署情况
     */
    public List<DeployedContainerInfo> getDeployedContainerInfos(String appId, String containerId) {
        ClusterStatusHolder clusterStatusHolder = getAppId2ClusterStatus().get(appId);
        if (clusterStatusHolder == null) {
            return Collections.emptyList();
        }
        return clusterStatusHolder.getDeployedContainerInfos(containerId);
    }

    private Map<String, WorkerInfo> getWorkerInfosByAppId(String appId) {
        ClusterStatusHolder clusterStatusHolder = getAppId2ClusterStatus().get(appId);
        if (clusterStatusHolder == null) {
            log.warn("[WorkerManagerService] can't find any worker for app(appId={}) yet.", appId);
            return Collections.emptyMap();
        }
        return clusterStatusHolder.getAllWorkers();
    }

    /**
     * filter invalid worker for job
     *
     * @param workerInfo worker info
     * @param jobInfo    job info
     * @return filter this worker when return true
     */
    private boolean filterWorker(WorkerInfo workerInfo, JobInfoDO jobInfo) {
        for (WorkerFilter filter : workerFilters) {
            if (filter.filter(workerInfo, jobInfo)) {
                return true;
            }
        }
        return false;
    }
}
