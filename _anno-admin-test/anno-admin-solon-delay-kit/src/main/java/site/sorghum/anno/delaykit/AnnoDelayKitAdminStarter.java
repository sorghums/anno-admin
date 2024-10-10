package site.sorghum.anno.delaykit;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;
import org.noear.solon.core.util.RunUtil;
import site.sorghum.anno.anno.annotation.global.AnnoScan;
import site.sorghum.anno.delaykit.client.DelayKitClient;
import site.sorghum.anno.delaykit.kit.DelayJobUtil;
import site.sorghum.anno.delaykit.kit.RegisterEntity;
import site.sorghum.anno.delaykit.kit.RemoteBaseJob;
import site.sorghum.anno.delaykit.kit.RemoteJobReq;
import site.sorghum.anno.delaykit.server.DelayKitJob;

import java.util.Map;

/**
 * Ano 管理入门
 *
 * @author Sorghum
 * @since 2023/06/05
 */
@SolonMain
@Slf4j
@AnnoScan
public class AnnoDelayKitAdminStarter {
    public static void main(String[] args) {
        Solon.start(AnnoDelayKitAdminStarter.class, args);
        DelayKitClient client = Solon.context().getBean(DelayKitClient.class);
        RunUtil.scheduleAtFixedRate(
            () -> {
                log.info("注册远程服务");
                Map<String, RemoteBaseJob> beansMapOfType = Solon.context().getBeansMapOfType(RemoteBaseJob.class);
                client.register(new RegisterEntity(new RegisterEntity.Register("demoService",
                    beansMapOfType.keySet()
                )));
            }, 200, 20000
        );
        log.info("调用客户端");
        try {
            DelayJobUtil.offer(
                RemoteJobReq.builder().jobName("demoJob").remoteParams(Map.of("key","value")).build(),
                DelayKitJob.class,200
            );
        } catch (Exception e) {
            log.error("error", e);
        }    }
}
