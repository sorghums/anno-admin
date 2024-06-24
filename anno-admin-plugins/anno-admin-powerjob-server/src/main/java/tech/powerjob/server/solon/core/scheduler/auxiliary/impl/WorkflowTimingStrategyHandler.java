package tech.powerjob.server.solon.core.scheduler.auxiliary.impl;

import org.noear.solon.annotation.Component;
import tech.powerjob.common.enums.TimeExpressionType;
import tech.powerjob.server.solon.core.scheduler.auxiliary.AbstractTimingStrategyHandler;

/**
 * @author Echo009
 * @since 2022/3/22
 */
@Component
public class WorkflowTimingStrategyHandler extends AbstractTimingStrategyHandler {
    @Override
    public TimeExpressionType supportType() {
        return TimeExpressionType.WORKFLOW;
    }
}
