package tech.powerjob.server.solon.moniter.monitors;

import org.noear.solon.annotation.Component;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import tech.powerjob.server.solon.common.aware.ServerInfoAware;
import tech.powerjob.server.solon.common.module.ServerInfo;
import tech.powerjob.server.solon.moniter.Event;
import tech.powerjob.server.solon.moniter.Monitor;

/**
 * 系统默认实现——基于日志的监控监视器
 * 需要接入方自行基于类 ELK 系统采集
 *
 * @author tjq
 * @since 2022/9/6
 */
@Component
public class LogMonitor implements Monitor, ServerInfoAware {

    /**
     * server 启动依赖 DB，DB会被 monitor，因此最初的几条 log serverInfo 一定为空，在此处简单防空
     */
    private ServerInfo serverInfo = new ServerInfo();

    private static final String MDC_KEY_SERVER_ID = "serverId";


    @Override
    public void init() {
    }

    @Override
    public void record(Event event) {
        MDC.put(MDC_KEY_SERVER_ID, String.valueOf(serverInfo.getId()));
        LoggerFactory.getLogger(event.type()).info(event.message());
    }

    @Override
    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }
}
