package site.sorghum.anno.delaykit.client;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.noear.snack.ONode;
import org.noear.socketd.SocketD;
import org.noear.socketd.transport.client.ClientSession;
import org.noear.socketd.transport.core.entity.StringEntity;
import org.noear.socketd.transport.core.listener.EventListener;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Result;
import site.sorghum.anno.delaykit.kit.RegisterEntity;
import site.sorghum.anno.delaykit.kit.RemoteBaseJob;
import site.sorghum.anno.delaykit.kit.RemoteJobReq;
import site.sorghum.anno.delaykit.server.DelayKitServer;

import java.io.IOException;

@Getter
@Component
@Slf4j
public class DelayKitClient {

    ClientSession session;

    @SneakyThrows
    public DelayKitClient() {
        Solon.context().getBeanAsync(DelayKitServer.class,
            d -> {
                try {
                    session = SocketD.createClient("sd:tcp://127.0.0.1:6956?serviceName=demoService&token=demoToken")
                        .listen(new EventListener().doOn("delay.run", (s, m) -> {
                            RemoteJobReq remoteJobReq = RemoteJobReq.toRemoteJobReq(m.entity());
                            log.info("收到远程调度任务:{}", ONode.serialize(remoteJobReq));
                            RemoteBaseJob remoteBaseJob = Solon.context().getBean(remoteJobReq.getJobName());
                            Result<?> run = remoteBaseJob.run(remoteJobReq);
                            s.replyEnd(m,new StringEntity(ONode.serialize(run)));
                        }))
                        .openOrThow();
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            });
    }

    @SneakyThrows
    public void register(RegisterEntity register){
        session.send("delay.register",register);
    }


}
