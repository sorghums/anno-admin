package tech.powerjob.server.solon.core.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.wood.annotation.Db;
import tech.powerjob.server.solon.persistence.remote.model.InstanceInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.JobInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.WorkflowInfoDO;
import tech.powerjob.server.solon.persistence.remote.repository.InstanceInfoRepository;
import tech.powerjob.server.solon.persistence.remote.repository.JobInfoRepository;
import tech.powerjob.server.solon.persistence.remote.repository.WorkflowInfoRepository;

import java.time.Duration;

/**
 * 本地缓存常用数据查询操作
 *
 * @author tjq
 * @since 2020/4/14
 */
@Slf4j
@Component
public class CacheService {

    @Db
    private JobInfoRepository jobInfoRepository;
    @Db
    private WorkflowInfoRepository workflowInfoRepository;
    @Db
    private InstanceInfoRepository instanceInfoRepository;

    private final Cache<String, String> jobId2JobNameCache;
    private final Cache<String, String> workflowId2WorkflowNameCache;
    private final Cache<String, String> instanceId2AppId;
    private final Cache<String, String> jobId2AppId;

    public CacheService() {

        jobId2JobNameCache = CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(1))
            .maximumSize(512)
            .softValues()
            .build();

        workflowId2WorkflowNameCache = CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(1))
            .maximumSize(512)
            .softValues()
            .build();

        instanceId2AppId = CacheBuilder.newBuilder()
            .maximumSize(1024)
            .softValues()
            .build();
        jobId2AppId = CacheBuilder.newBuilder()
            .maximumSize(1024)
            .softValues()
            .build();
    }

    /**
     * 根据 jobId 查询 jobName（不保证数据一致性，或者说只要改了数据必不一致hhh）
     *
     * @param jobId 任务ID
     * @return 任务名称
     */
    public String getJobName(String jobId) {
        try {
            return jobId2JobNameCache.get(jobId, () -> {
                JobInfoDO jobInfoDO = jobInfoRepository.selectById(jobId);
                // 防止缓存穿透 hhh（但是一开始没有，后来创建的情况下会有问题，不过问题不大，这里就不管了）
                if (jobInfoDO != null) {
                    return jobInfoDO.getJobName();
                }
                return "";
            });
        } catch (Exception e) {
            log.error("[CacheService] getJobName for {} failed.", jobId, e);
        }
        return null;
    }

    /**
     * 根据 workflowId 查询 工作流名称
     *
     * @param workflowId 工作流ID
     * @return 工作流名称
     */
    public String getWorkflowName(String workflowId) {
        try {
            return workflowId2WorkflowNameCache.get(workflowId, () -> {
                WorkflowInfoDO workflowInfoDO = workflowInfoRepository.selectById(workflowId);
                if (workflowInfoDO != null) {
                    return workflowInfoDO.getWfName();
                }
                // 防止缓存穿透 hhh（但是一开始没有，后来创建的情况下会有问题，不过问题不大，这里就不管了）
                return "";
            });
        } catch (Exception e) {
            log.error("[CacheService] getWorkflowName for {} failed.", workflowId, e);
        }
        return null;
    }

    public String getAppIdByInstanceId(String instanceId) {

        try {
            return instanceId2AppId.get(instanceId, () -> {
                // 内部记录数据库异常
                try {
                    InstanceInfoDO instanceLog = instanceInfoRepository.findByInstanceId(instanceId);
                    if (instanceLog != null) {
                        return instanceLog.getAppId();
                    }
                } catch (Exception e) {
                    log.error("[CacheService] getAppId for instanceId:{} failed.", instanceId, e);
                }
                return null;
            });
        } catch (Exception ignore) {
            // 忽略缓存 load 失败的异常
        }
        return null;
    }

    public String getAppIdByJobId(String jobId) {
        try {
            return jobId2AppId.get(jobId, () -> {
                try {
                    JobInfoDO jobInfoDO = jobInfoRepository.selectById(jobId);
                    if (jobInfoDO != null) {
                        return jobInfoDO.getAppId();
                    }
                    return "";
                } catch (Exception e) {
                    log.error("[CacheService] getAppId for job:{} failed.", jobId, e);
                }
                return null;
            });
        } catch (Exception ignore) {
        }
        return null;
    }
}
