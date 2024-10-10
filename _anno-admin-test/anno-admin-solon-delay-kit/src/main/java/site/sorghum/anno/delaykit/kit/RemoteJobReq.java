package site.sorghum.anno.delaykit.kit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.noear.snack.ONode;
import org.noear.socketd.transport.core.Entity;
import org.noear.socketd.transport.core.entity.StringEntity;
import org.noear.solon.annotation.Note;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoteJobReq {

    @Note("任务序号")
    String jobNo;

    @Note("任务ID")
    String jobName;

    @Note("任务参数")
    Map<String,Object> remoteParams;

    public static Entity toEntity(RemoteJobReq remoteJobReq) {
        if (remoteJobReq.getRemoteParams() == null){
            remoteJobReq.setRemoteParams(new HashMap<>());
        }
        if (!(remoteJobReq.getRemoteParams() instanceof HashMap)) {
            remoteJobReq.setRemoteParams(new HashMap<>(remoteJobReq.getRemoteParams()));
        }
        return new StringEntity(ONode.serialize(remoteJobReq));
    }

    public static RemoteJobReq toRemoteJobReq(Entity entity) {
        return ONode.deserialize(entity.dataAsString(), RemoteJobReq.class);
    }
}
