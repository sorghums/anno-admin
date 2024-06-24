package tech.powerjob.server.solon.core.uid;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.bean.InitializingBean;
import tech.powerjob.server.solon.remote.server.self.ServerInfoService;

/**
 * 唯一ID生成服务，使用 Twitter snowflake 算法
 * 机房ID：固定为0，占用2位
 * 机器ID：由 ServerIdProvider 提供
 *
 * @author tjq
 * @since 2020/4/6
 */
@Slf4j
@Component
public class IdGenerateService implements InitializingBean {

    private SnowFlakeIdGenerator snowFlakeIdGenerator;

    @Inject
    private ServerInfoService serverInfoService;

    private static final int DATA_CENTER_ID = 0;

    @Override
    public void afterInjection() {
        long id = serverInfoService.fetchServiceInfo().getId();
        snowFlakeIdGenerator = new SnowFlakeIdGenerator(DATA_CENTER_ID, id);
        log.info("[IdGenerateService] initialize IdGenerateService successfully, ID:{}", id);
    }

    /**
     * 分配分布式唯一ID
     *
     * @return 分布式唯一ID
     */
    public String allocate() {
        return String.valueOf(snowFlakeIdGenerator.nextId());
    }

}
