package site.sorghum.anno.solon.filter;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._common.util.AnnoContextUtil;
import site.sorghum.anno.anno.util.ReentrantStopWatch;

import java.util.concurrent.TimeUnit;

/**
 * 清除上下文，index 设置为最大值，保证最后执行
 */
@Slf4j
@Component(index = Integer.MAX_VALUE - 1)
public class AnnoContextClearFilter implements Filter {

    @Inject
    private AnnoProperty annoProperty;

    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        try {
            chain.doFilter(ctx);
        } finally {
            AnnoContextUtil.AnnoContext context = AnnoContextUtil.getContext();
            ReentrantStopWatch stopWatch = context.getStopWatch();

            if (context.isPrintDetailLog() && stopWatch != null
                && stopWatch.getTotalTimeMillis() > annoProperty.getDetailLogThreshold()) {

                log.info("{}", stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
            }
            AnnoContextUtil.clearContext();
        }
    }
}
