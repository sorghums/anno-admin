package site.sorghum.anno.delaykit.server;

import cn.hutool.core.util.RandomUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.noear.socketd.SocketD;
import org.noear.socketd.transport.core.Reply;
import org.noear.socketd.transport.core.Session;
import org.noear.socketd.transport.core.entity.StringEntity;
import org.noear.socketd.transport.core.listener.EventListener;
import org.noear.socketd.transport.server.Server;
import org.noear.socketd.utils.RunUtils;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno.delaykit.kit.RegisterEntity;
import site.sorghum.anno.delaykit.kit.RemoteJobReq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class DelayKitServer {
    @Inject
    JobRegister jobRegister;

    @Inject("${kit.remoteExecuteTimeout:60}")
    Long remoteExecuteTimeout;

    private final static HashMap<String, List<Session>> SESSION_MAP = new HashMap<>();

    public DelayKitServer() throws IOException {
        int serverPort = Solon.cfg().getInt("kit.tcp", 6956);
        Server server = SocketD.createServer("sd:tcp")
            .config(
                c -> c.port(serverPort)
            )
            .listen(new EventListener().doOnOpen(
                s -> {
                    String token = s.param("token");
                    String serviceName = s.param("serviceName");
                    System.out.println("收到客户端请求，token=" + token);
                    SESSION_MAP.getOrDefault(
                        serviceName,
                        SESSION_MAP.putIfAbsent(serviceName, new ArrayList<>())
                    ).add(s);
                }
            ).doOn("delay.runJob", (s, m) -> {
                s.replyEnd(m, new StringEntity("ok"));
            }).doOn("delay.register", (s, m) -> {
                log.info("收到注册请求，{}", s.remoteAddress());
                RegisterEntity entity = RegisterEntity.of(m.entity());
                RegisterEntity.Register register = entity.getRegister();
                jobRegister.register(register.getServiceName(), register.getJobNames());
                s.replyEnd(m, new StringEntity("ok"));
            }))
            .start();
        //添加安全停止
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            RunUtils.runAndTry(server::stop);
        }));
    }

    @SneakyThrows
    public Reply callClient(Session session, RemoteJobReq request) {
        return session.sendAndRequest(
            "delay.run",
            RemoteJobReq.toEntity(request),
            remoteExecuteTimeout).await();
    }

    public Session getSession(String serviceName) {
        List<Session> sessions = SESSION_MAP.get(serviceName);
        if (sessions == null || sessions.isEmpty()) {
            throw new IllegalArgumentException("对应服务未成功启动");
        }
        return RandomUtil.randomEle(sessions, 1);
    }
}