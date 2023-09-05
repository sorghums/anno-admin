package tech.powerjob.server.solon.common.aware;

import tech.powerjob.server.solon.common.module.ServerInfo;

/**
 * notify server info
 *
 * @author tjq
 * @since 2022/9/12
 */
public interface ServerInfoAware extends PowerJobAware {

    void setServerInfo(ServerInfo serverInfo);
}
