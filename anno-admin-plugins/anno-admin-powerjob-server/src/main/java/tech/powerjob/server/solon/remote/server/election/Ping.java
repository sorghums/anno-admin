package tech.powerjob.server.solon.remote.server.election;

import lombok.Data;
import tech.powerjob.common.PowerSerializable;


/**
 * 检测目标机器是否存活
 *
 * @author tjq
 * @since 2020/4/5
 */
@Data
public class Ping implements PowerSerializable {
    private long currentTime;
}
