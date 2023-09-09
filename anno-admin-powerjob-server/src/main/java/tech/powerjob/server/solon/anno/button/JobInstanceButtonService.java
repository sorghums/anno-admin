package tech.powerjob.server.solon.anno.button;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import tech.powerjob.server.solon.core.instance.InstanceLogService;
import tech.powerjob.server.solon.core.instance.InstanceService;
import tech.powerjob.server.solon.persistence.StringPage;

import java.util.Map;

/**
 * @author songyinyin
 * @since 2023/9/7 11:27
 */
@Component
public class JobInstanceButtonService {

    @Inject
    InstanceLogService instanceLogService;
    @Inject
    InstanceService instanceService;

    /**
     * 查看日志
     */
    public void fetchInstanceLog(Map<String, Object> props) {
        String appId = (String) props.get("appId");
        String instanceId = (String) props.get("instanceId");
        Long logIndex = (Long) props.get("logIndex");
        if (logIndex == null) {
            logIndex = 0L;
        }
        StringPage stringPage = instanceLogService.fetchInstanceLog(appId, instanceId, logIndex);
        System.out.println(stringPage);
    }

    /**
     * 重试
     */
    public void retryInstance(Map<String, Object> props) {
        String appId = (String) props.get("appId");
        String instanceId = (String) props.get("instanceId");
        instanceService.retryInstance(appId, instanceId);
    }
}
