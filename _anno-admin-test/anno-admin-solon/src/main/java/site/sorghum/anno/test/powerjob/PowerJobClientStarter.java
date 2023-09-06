package site.sorghum.anno.test.powerjob;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.AppContext;
import org.noear.solon.extend.powerjob.impl.PowerJobProperties;
import org.noear.solon.extend.powerjob.impl.PowerJobWorkerOfSolon;
import org.noear.solon.scheduling.annotation.Retry;
import tech.powerjob.client.PowerJobClient;
import tech.powerjob.common.exception.PowerJobException;
import tech.powerjob.common.utils.CommonUtils;
import tech.powerjob.worker.common.PowerJobWorkerConfig;

/**
 * @author songyinyin
 * @since 2023/9/6 11:49
 */
@Slf4j
@Component
public class PowerJobClientStarter {


  @Retry(include = PowerJobException.class, maxAttempts = 10, interval = 30)
  public void start(AppContext context, PowerJobProperties properties) throws Throwable {
    try {
      start0(context, properties);
    } catch (PowerJobException e) {
      log.error("PowerJobClient start failed, retrying... ", e);
      throw e;
    }
  }

  public void start0(AppContext context, PowerJobProperties properties) {
    CommonUtils.requireNonNull(properties.getServerAddress(), "serverAddress can't be empty! " +
        "if you don't want to enable powerjob, please config program arguments: solon.powerjob.worker.enabled=false");

    PowerJobWorkerConfig config = properties.toConfig();

    if (StringUtils.isNotBlank(properties.getPassword())) {
      // Create PowerJobClient object
      PowerJobClient client = new PowerJobClient(config.getServerAddress(), config.getAppName(), properties.getPassword());
      context.beanInject(client);
      context.wrapAndPut(PowerJobClient.class, client); //包装并注册到容器（如果做为临时变量，会被回收的）
    }

    /*
     * Create PowerJobWorkerOfSolon object and inject it into Solon.
     */
    PowerJobWorkerOfSolon worker = new PowerJobWorkerOfSolon(context, config);
    context.beanInject(worker);
    context.wrapAndPut(PowerJobWorkerOfSolon.class, worker); //包装并注册到容器（如果做为临时变量，会被回收的）
  }
}
