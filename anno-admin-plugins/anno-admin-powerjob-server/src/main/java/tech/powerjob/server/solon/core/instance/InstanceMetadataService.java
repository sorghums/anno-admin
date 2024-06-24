package tech.powerjob.server.solon.core.instance;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.bean.LifecycleBean;
import org.noear.wood.annotation.Db;
import tech.powerjob.server.solon.persistence.remote.model.InstanceInfoDO;
import tech.powerjob.server.solon.persistence.remote.model.JobInfoDO;
import tech.powerjob.server.solon.persistence.remote.repository.InstanceInfoRepository;
import tech.powerjob.server.solon.persistence.remote.repository.JobInfoRepository;

import java.util.concurrent.ExecutionException;

/**
 * 存储 instance 对应的 JobInfo 信息
 *
 * @author tjq
 * @since 2020/6/23
 */
@Component
public class InstanceMetadataService implements LifecycleBean {

    @Db
    private JobInfoRepository jobInfoRepository;

    @Db
    private InstanceInfoRepository instanceInfoRepository;

    /**
     * 缓存，一旦生成任务实例，其对应的 JobInfo 不应该再改变（即使源数据改变）
     */
    private Cache<String, JobInfoDO> instanceId2JobInfoCache;

    @Inject("${oms.instance.metadata.cache.size:2048}")
    private int instanceMetadataCacheSize;
    private static final int CACHE_CONCURRENCY_LEVEL = 16;

    @Override
    public void start() throws Exception {
        instanceId2JobInfoCache = CacheBuilder.newBuilder()
                .concurrencyLevel(CACHE_CONCURRENCY_LEVEL)
                .maximumSize(instanceMetadataCacheSize)
                .softValues()
                .build();
    }

    /**
     * 根据 instanceId 获取 JobInfo
     * @param instanceId instanceId
     * @return JobInfoDO
     * @throws ExecutionException 异常
     */
    public JobInfoDO fetchJobInfoByInstanceId(String instanceId) throws ExecutionException {
        return instanceId2JobInfoCache.get(instanceId, () -> {
            InstanceInfoDO instanceInfo = instanceInfoRepository.findByInstanceId(instanceId);
            if (instanceInfo != null) {
                JobInfoDO jobInfoDO = jobInfoRepository.selectById(instanceInfo.getJobId());
                if (jobInfoDO == null) {
                    throw new IllegalArgumentException("can't find JobInfo by jobId: " + instanceInfo.getJobId());
                }
                return jobInfoDO;
            }
            throw new IllegalArgumentException("can't find Instance by instanceId: " + instanceId);
        });
    }

    /**
     * 装载缓存
     * @param instanceId instanceId
     * @param jobInfoDO 原始的任务数据
     */
    public void loadJobInfo(String instanceId, JobInfoDO jobInfoDO) {
        instanceId2JobInfoCache.put(instanceId, jobInfoDO);
    }

    /**
     * 失效缓存
     * @param instanceId instanceId
     */
    public void invalidateJobInfo(String instanceId) {
        instanceId2JobInfoCache.invalidate(instanceId);
    }

}
