package site.sorghum.anno.cmd;

import jakarta.inject.Inject;
import org.dromara.warm.flow.core.exception.FlowException;
import org.dromara.warm.flow.core.service.DefService;
import org.noear.solon.annotation.Component;
import site.sorghum.anno._common.entity.CommonParam;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;
import site.sorghum.anno.ao.FlowDefinitionAo;

@Component
public class FlowDefinitionUnPublishCmd implements JavaCmdSupplier {
    @Inject
    DefService defService;
    @Override
    public String run(CommonParam param) {
        try {
            FlowDefinitionAo definitionAo = param.toT(FlowDefinitionAo.class);
            defService.unPublish(definitionAo.getId());
            return "发布成功！";
        }catch (FlowException e){
            throw new BizException(e.getMessage(),e);
        }
    }
}
