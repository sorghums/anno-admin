package site.sorghum.anno.solon.filter;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._common.util.AnnoContextUtil;

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
        if (!ctx.path().contains(AnnoConstants.BASE_URL)) {
            chain.doFilter(ctx);
            return;
        }
        try {
            chain.doFilter(ctx);
        } finally {
            AnnoContextUtil.printLog(log, annoProperty.getDetailLogThreshold());
            AnnoContextUtil.clearContext();
        }
    }
}
