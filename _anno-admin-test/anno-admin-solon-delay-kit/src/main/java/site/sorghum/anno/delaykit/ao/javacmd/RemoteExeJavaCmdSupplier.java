package site.sorghum.anno.delaykit.ao.javacmd;

import org.noear.solon.annotation.Component;
import site.sorghum.anno._common.entity.CommonParam;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;
import site.sorghum.anno.delaykit.ao.RemoteJobAo;
import site.sorghum.anno.delaykit.kit.DelayJobUtil;
import site.sorghum.anno.delaykit.kit.RemoteJobReq;
import site.sorghum.anno.delaykit.server.DelayKitJob;

import java.util.Map;

@Component
public class RemoteExeJavaCmdSupplier implements JavaCmdSupplier {

    @Override
    public String run(CommonParam param) {
        RemoteJobAo remoteJobAo = param.toT(RemoteJobAo.class);
        DelayJobUtil.offer(
            RemoteJobReq.builder().
                jobName(remoteJobAo.getJobName()).
                remoteParams(Map.of("demo","demo")).build(),
            DelayKitJob.class,1
        );
        return defaultMsgSuccess("执行成功!");
    }
}
