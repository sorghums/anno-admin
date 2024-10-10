package site.sorghum.anno.delaykit.kit;

import org.noear.solon.core.handle.Result;

/**
 * 基础工作
 *
 * @author Sorghum
 * @since 2023/07/24
 */
public interface RemoteBaseJob {

    public Result<?> run(RemoteJobReq remoteJobReq);

}
