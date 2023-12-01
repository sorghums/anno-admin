package site.sorghum.anno.solon.config;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import org.noear.solon.web.cors.CrossHandler;

/**
 * 跨域处理程序
 *
 * @author Sorghum
 * @since 2023/12/01
 */
@Component(index = Integer.MIN_VALUE)
public class AnnoCrossFilter implements Filter {
    CrossHandler crossHandler = new CrossHandler().allowedOrigins("*").allowedMethods("*").allowedHeaders("*");
    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        crossHandler.handle(ctx);
        chain.doFilter(ctx);
    }
}
