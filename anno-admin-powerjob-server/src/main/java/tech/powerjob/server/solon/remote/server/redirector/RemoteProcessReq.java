package tech.powerjob.server.solon.remote.server.redirector;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import tech.powerjob.common.PowerSerializable;

/**
 * 原创执行命令
 *
 * @author tjq
 * @since 12/13/20
 */
@Getter
@Setter
@Accessors(chain = true)
public class RemoteProcessReq implements PowerSerializable {

    private String className;
    private String methodName;
    private String[] parameterTypes;

    private Object[] args;

}
