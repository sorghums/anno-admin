package site.sorghum.anno.cmd;

import jakarta.inject.Inject;
import org.dromara.warm.flow.core.service.DefService;
import org.noear.solon.annotation.Component;
import site.sorghum.anno._common.entity.CommonParam;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;
import site.sorghum.anno.ao.FlowDefinitionAo;

@Component
public class FlowDefinitionPublishCmd implements JavaCmdSupplier {
    @Inject
    DefService defService;

    @Override
    public String run(CommonParam param) {
        FlowDefinitionAo definitionAo = param.toT(FlowDefinitionAo.class);
        defService.publish(definitionAo.getId());
        return "发布成功！";
    }
}
