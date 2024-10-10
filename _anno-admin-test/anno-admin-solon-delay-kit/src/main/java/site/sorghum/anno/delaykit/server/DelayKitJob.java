package site.sorghum.anno.delaykit.server;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import jodd.exception.ExceptionUtil;
import org.noear.snack.ONode;
import org.noear.socketd.transport.core.Reply;
import org.noear.socketd.transport.core.Session;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Result;
import site.sorghum.anno.delaykit.ao.JobHistoryAo;
import site.sorghum.anno.delaykit.ao.dao.JobHistoryAoDao;
import site.sorghum.anno.delaykit.kit.BaseJob;
import site.sorghum.anno.delaykit.kit.JobEntity;
import site.sorghum.anno.delaykit.kit.RemoteJobReq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class DelayKitJob implements BaseJob {

    @Inject
    private DelayKitServer delayKitServer;
    @Inject
    JobRegister jobRegister;
    @Inject
    JobHistoryAoDao jobHistoryAoDao;

    @Override
    public void run(JobEntity jobEntity) throws IOException {
        RemoteJobReq req = jobEntity.dataTo(RemoteJobReq.class);
        if (StrUtil.isBlank(req.getJobNo())) {
            req.setJobNo(IdUtil.getSnowflakeNextIdStr());
        }
        JobHistoryAo jobHistoryAo = new JobHistoryAo();
        jobHistoryAo.setJobId(req.getJobNo());
        jobHistoryAo.setJobName(req.getJobName());
        jobHistoryAo.setStatus(1);
        jobHistoryAo.setMaxCount(jobEntity.getMaxCount());
        jobHistoryAo.setNowCount(jobEntity.getNowCount());
        // 防止anno展示错误
        req.getRemoteParams().put("@annoIgnore", true);
        jobHistoryAo.setRemoteParams(ONode.serialize(req.getRemoteParams()));
        jobHistoryAo.setInterval(jobEntity.getInterval());
        jobHistoryAo.setLastRunTime(jobEntity.getLastRunTime());
        jobHistoryAo.setFirstRunTime(jobEntity.getFirstRunTime());
        jobHistoryAo.setRunTime(jobEntity.getRunTime());
        try {
            String jobName = req.getJobName();
            Set<String> service = jobRegister.job2Service(jobName);
            if (CollUtil.isEmpty(service)) {
                throw new IllegalArgumentException("jobName not found service");
            }
            String serviceName = RandomUtil.randomEle(new ArrayList<>(service), 1);
            Session session = delayKitServer.getSession(serviceName);
            jobHistoryAo.setService(serviceName);
            jobHistoryAo.setRemoteAddr(session.remoteAddress().toString());
            Reply reply = delayKitServer.callClient(session, req);
            HashMap<String, Object> result = ONode.deserialize(reply.dataAsString(), HashMap.class);
            Result<?> actualRes = ONode.deserialize(reply.dataAsString(), Result.class);
            result.put("@annoIgnore", true);
            jobHistoryAo.setResult(ONode.serialize(result));
            jobHistoryAo.setStatus(1);
            if (actualRes.getCode() != 200) {
                throw new IllegalStateException("jobName run error:" + actualRes.getDescription());
            }
            jobHistoryAoDao.insert(jobHistoryAo);
        } catch (Throwable ex) {
            jobHistoryAo.setStatus(0);
            jobHistoryAo.setException(ONode.serialize(Map.of(
                "@annoIgnore", true,
                "error", ex.getMessage(),
                "stackTrace", ExceptionUtil.exceptionStackTraceToString(ex)
            )));
            jobHistoryAoDao.insert(jobHistoryAo);
            throw ex;
        }finally {
            jobEntity.setInterval(3);
            jobEntity.dataIn(req);
        }

    }
}
