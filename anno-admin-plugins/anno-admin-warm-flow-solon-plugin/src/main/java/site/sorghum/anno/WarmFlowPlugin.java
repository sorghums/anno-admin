package site.sorghum.anno;

import lombok.SneakyThrows;
import org.dromara.warm.flow.core.dto.FlowParams;
import org.dromara.warm.flow.core.entity.Instance;
import org.dromara.warm.flow.core.service.DefService;
import org.dromara.warm.flow.core.service.InsService;
import org.dromara.warm.flow.orm.entity.FlowInstance;
import org.noear.solon.Solon;
import org.pf4j.Extension;
import site.sorghum.anno.ao.CopyHisTaskAo;
import site.sorghum.anno.ao.DoneHisTaskAo;
import site.sorghum.anno.ao.FlowDefinitionAo;
import site.sorghum.anno.ao.WaitFlowTaskAo;
import site.sorghum.anno.plugin.AnPluginMenu;
import site.sorghum.anno.plugin.AnnoPlugin;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author songyinyin
 * @since 2023/9/5 12:39
 */
@Extension
public class WarmFlowPlugin extends AnnoPlugin {

    public WarmFlowPlugin() {
        super("warm-flow", "warm-flow 的 anno-admin 版本");
    }

    @Override
    public List<AnPluginMenu> initEntityMenus() {
        List<AnPluginMenu> list  = new ArrayList<>();
        list.add(createRootMenu("warm-flow", "WarmFlow", "layui-icon layui-icon-diamond", 20));
        list.add(createEntityMenu(FlowDefinitionAo.class, list.get(0).getId(), "layui-icon layui-icon-cart", 200));
        list.add(createEntityMenu(WaitFlowTaskAo.class, list.get(0).getId(), "layui-icon layui-icon-note", 205));
        list.add(createEntityMenu(DoneHisTaskAo.class, list.get(0).getId(), "layui-icon layui-icon-note", 210));
        list.add(createEntityMenu(CopyHisTaskAo.class, list.get(0).getId(), "layui-icon layui-icon-note", 215));
        return list;
    }

    @SneakyThrows
    @Override
    public void run() {
        DefService defService = Solon.context().getBean(DefService.class);
        Long id = defService.importXml(new FileInputStream("D:\\Project\\rep\\opensource\\anno-admin\\anno-admin-plugins\\anno-admin-warm-flow-solon-plugin\\src\\main\\resources\\demo\\leaveFlow-serial1.xml")).getId();
        defService.publish(id);
        InsService insService = Solon.context().getBean(InsService.class);
        Instance instance = insService.start("1",
            new FlowParams().flowCode("leaveFlow-serial1"));
        insService.skipByInsId(instance.getId(),new FlowParams().skipType("PASS").flowCode("leaveFlow-serial1").handler("1666356287765979136").permissionFlag(List.of("role:1")));
        FlowInstance instances = new FlowInstance();
        insService.list(instances);
        super.run();
    }
}
