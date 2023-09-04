package tech.powerjob.server.solon.core.scheduler.auxiliary.impl;

import org.noear.solon.annotation.Component;
import tech.powerjob.server.solon.core.scheduler.auxiliary.AbstractTimingStrategyHandler;
import tech.powerjob.common.enums.TimeExpressionType;

/**
 * @author Echo009
 * @since 2022/3/22
 */
@Component
public class ApiTimingStrategyHandler extends AbstractTimingStrategyHandler {
    @Override
    public TimeExpressionType supportType() {
        return TimeExpressionType.API;
    }
}
