package tech.powerjob.server.solon.common.module;

import lombok.Data;

/**
 * current server info
 *
 * @author tjq
 * @since 2022/9/12
 */
@Data
public class ServerInfo {

    private Long id;

    private String ip;

    private long bornTime;

    private String version = "UNKNOWN";
}
