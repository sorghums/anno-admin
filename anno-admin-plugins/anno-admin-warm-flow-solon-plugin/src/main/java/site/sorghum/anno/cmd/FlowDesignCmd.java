package site.sorghum.anno.cmd;

import org.noear.solon.annotation.Component;
import site.sorghum.anno._common.entity.CommonParam;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;
import site.sorghum.anno.ao.FlowDefinitionAo;

@Component
public class FlowDesignCmd implements JavaCmdSupplier {
    @Override
    public String run(CommonParam param) {
        FlowDefinitionAo definitionAo = param.toT(FlowDefinitionAo.class);
        return iframeUrl("/warm-flow-ui/index.html?id=%s&disabled=%s".formatted(definitionAo.getId(),"false"));
    }
}
