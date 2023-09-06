package site.sorghum.anno.solon.filter;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import site.sorghum.anno._common.util.AnnoContextUtil;

@Component
public class AnnoContextClearFilter implements Filter {
    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        try {
            chain.doFilter(ctx);
        } finally {
            AnnoContextUtil.clearContext();
        }
    }
}
