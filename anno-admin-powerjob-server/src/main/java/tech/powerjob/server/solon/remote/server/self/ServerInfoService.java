package tech.powerjob.server.solon.remote.server.self;

import tech.powerjob.server.solon.common.module.ServerInfo;

/**
 * ServerInfoService
 *
 * @author tjq
 * @since 2022/9/12
 */
public interface ServerInfoService {

    /**
     * fetch current server info
     *
     * @return ServerInfo
     */
    ServerInfo fetchServiceInfo();

}
