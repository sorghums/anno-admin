package site.sorghum.anno.delaykit.kit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.noear.snack.ONode;
import org.noear.socketd.transport.core.Entity;
import org.noear.socketd.transport.core.entity.StringEntity;
import org.noear.solon.annotation.Note;

import java.util.Collection;

public class RegisterEntity extends StringEntity {

    /**
     * 将实体转换为注册实体
     * @param entityDefault 实体
     * @return 注册实体
     */
    public static RegisterEntity of(Entity entityDefault){
        return new RegisterEntity(entityDefault.dataAsString());
    }

    public RegisterEntity(Register register) {
        super(ONode.serialize(register));
    }

    public RegisterEntity(String data) {
        super(data);
    }

    public Register getRegister() {
        return ONode.deserialize(dataAsString(),Register.class);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Register{
        @Note("服务名")
        String serviceName;
        @Note("任务名")
        Collection<String> jobNames;

    }
}
