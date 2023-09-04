package tech.powerjob.server.solon.remote.transporter.impl;

import tech.powerjob.remote.framework.base.Address;
import tech.powerjob.remote.framework.base.HandlerLocation;
import tech.powerjob.remote.framework.base.ServerType;
import tech.powerjob.remote.framework.base.URL;

import static tech.powerjob.common.RemoteConstant.S4S_HANDLER_PING;
import static tech.powerjob.common.RemoteConstant.S4S_HANDLER_PROCESS;
import static tech.powerjob.common.RemoteConstant.S4S_PATH;
import static tech.powerjob.common.RemoteConstant.WORKER_HANDLER_DEPLOY_CONTAINER;
import static tech.powerjob.common.RemoteConstant.WORKER_HANDLER_DESTROY_CONTAINER;
import static tech.powerjob.common.RemoteConstant.WORKER_PATH;
import static tech.powerjob.common.RemoteConstant.WTT_HANDLER_QUERY_INSTANCE_STATUS;
import static tech.powerjob.common.RemoteConstant.WTT_HANDLER_RUN_JOB;
import static tech.powerjob.common.RemoteConstant.WTT_HANDLER_STOP_INSTANCE;

/**
 * 统一生成地址
 *
 * @author tjq
 * @since 2023/1/21
 */
public class ServerURLFactory {

    public static URL dispatchJob2Worker(String address) {
        return simileBuild(address, ServerType.WORKER, WORKER_PATH, WTT_HANDLER_RUN_JOB);
    }

    public static URL stopInstance2Worker(String address) {
        return simileBuild(address, ServerType.WORKER, WORKER_PATH, WTT_HANDLER_STOP_INSTANCE);
    }

    public static URL queryInstance2Worker(String address) {
        return simileBuild(address, ServerType.WORKER, WORKER_PATH, WTT_HANDLER_QUERY_INSTANCE_STATUS);
    }

    public static URL deployContainer2Worker(String address) {
        return simileBuild(address, ServerType.WORKER, WORKER_PATH, WORKER_HANDLER_DEPLOY_CONTAINER);
    }

    public static URL destroyContainer2Worker(String address) {
        return simileBuild(address, ServerType.WORKER, WORKER_PATH, WORKER_HANDLER_DESTROY_CONTAINER);
    }

    public static URL ping2Friend(String address) {
        return simileBuild(address, ServerType.SERVER, S4S_PATH, S4S_HANDLER_PING);
    }

    public static URL process2Friend(String address) {
        return simileBuild(address, ServerType.SERVER, S4S_PATH, S4S_HANDLER_PROCESS);
    }

    public static URL simileBuild(String address, ServerType type, String rootPath, String handlerPath) {
        return new URL()
                .setServerType(type)
                .setAddress(Address.fromIpv4(address))
                .setLocation(new HandlerLocation().setRootPath(rootPath).setMethodPath(handlerPath));
    }
}
