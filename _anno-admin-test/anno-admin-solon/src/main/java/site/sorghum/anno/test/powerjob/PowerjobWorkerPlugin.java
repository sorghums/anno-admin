package site.sorghum.anno.test.powerjob;

import org.apache.commons.lang3.StringUtils;
import org.noear.solon.Solon;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.event.AppLoadEndEvent;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.extend.powerjob.impl.PowerJobBeanBuilder;
import org.noear.solon.extend.powerjob.impl.PowerJobProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.powerjob.solon.annotation.PowerJob;

/**
 * @author songyinyin
 * @since 2023/9/5 22:37
 */
public class PowerjobWorkerPlugin implements Plugin {
  private static final Logger logger = LoggerFactory.getLogger(PowerjobWorkerPlugin.class);

  //::::把旧的配置，前缀直接改掉（换个喜欢的）
  private static final String configStarts = "solon.powerjob2";

  @Override
  public void start(AppContext context) throws Throwable {
    PowerJobProperties properties = context.cfg().getBean(configStarts, PowerJobProperties.class);

    if (!properties.isEnabled()) {
      logger.warn("PowerJob is disabled, powerjob worker will not start.");
      return;
    }

    if (StringUtils.isBlank(properties.getAppName())) {
      //如果没有配置 appName，则使用 solon.app.name 配置
      properties.setAppName(Solon.cfg().appName());
    }

    if (StringUtils.isBlank(properties.getAppName())) {
      logger.error("PowerJob app Name is empty, powerjob worker will not start.");
      return;
    }

    //Add anno support
    context.beanBuilderAdd(PowerJob.class, new PowerJobBeanBuilder());

    //::::调后启用服务（重点在这儿，比 solon.boot.undertow 启动要晚）
    EventBus.subscribe(AppLoadEndEvent.class, e -> {
      startDo(context, properties);
    });
  }

  private void startDo(AppContext context, PowerJobProperties properties) throws Throwable {
    context.getBean(PowerJobClientStarter.class).start(context, properties);
  }
}

