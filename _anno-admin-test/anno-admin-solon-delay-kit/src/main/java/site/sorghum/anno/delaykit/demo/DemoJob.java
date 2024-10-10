package site.sorghum.anno.delaykit.demo;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Result;
import site.sorghum.anno.delaykit.kit.RemoteBaseJob;
import site.sorghum.anno.delaykit.kit.RemoteJobReq;

import java.util.Map;

@Component(name = "测试Demo")
public class DemoJob implements RemoteBaseJob {

    @Override
    public Result<?> run(RemoteJobReq remoteJobReq) {
        Map<String, Object> remoteParams = remoteJobReq.getRemoteParams();
        System.out.println("remoteParams = " + remoteParams);
        return Result.succeed("success");
    }
}
