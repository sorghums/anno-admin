package site.sorghum.anno.cmd;

import lombok.SneakyThrows;
import org.dromara.warm.flow.core.service.ChartService;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno._common.entity.CommonParam;
import site.sorghum.anno.anno.javacmd.JavaCmdSupplier;

@Component
public class GetFlowImgCmd implements JavaCmdSupplier {

    @Inject
    ChartService chartService;
    @SneakyThrows
    @Override
    public String run(CommonParam param) {
        Long instanceId = param.getLong("instanceId");
        String flowChart = chartService.chartIns(instanceId);
        return iframeDoc("""
            <img src="data:image/gif;base64,%s" style="width: 100%%;">
            """.formatted(flowChart).trim());
    }
}
