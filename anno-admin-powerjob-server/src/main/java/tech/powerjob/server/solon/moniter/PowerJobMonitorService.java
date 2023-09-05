package tech.powerjob.server.solon.moniter;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.bean.LifecycleBean;

import java.util.List;

/**
 * PowerJob 服务端监控
 *
 * @author tjq
 * @since 2022/9/10
 */
@Slf4j
@Component
public class PowerJobMonitorService implements MonitorService, LifecycleBean {

    private final List<Monitor> monitors = Lists.newLinkedList();

    @Override
    public void start() {
        List<Monitor> beans = Solon.context().getBeansOfType(Monitor.class);
        beans.forEach(m -> {
            log.info("[MonitorService] register monitor: {}", m.getClass().getName());
            this.monitors.add(m);
        });
    }

    @Override
    public void monitor(Event event) {
        monitors.forEach(m -> m.record(event));
    }
}
