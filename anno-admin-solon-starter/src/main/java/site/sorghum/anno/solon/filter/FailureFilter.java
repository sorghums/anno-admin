package site.sorghum.anno.solon.filter;


import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.SaTokenException;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import org.noear.solon.validation.ValidatorException;
import org.noear.solon.validation.annotation.Logined;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.AnnoContextUtil;

import java.time.format.DateTimeParseException;

/**
 * 全局异常拦截
 *
 * @author Sorghum
 * @since 2023/02/24
 */
@Component
@Slf4j
public class FailureFilter implements Filter {
    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        try {
            chain.doFilter(ctx);
            // 请求完成后自动清除上下文
            AnnoContextUtil.clearContext();
        } catch (ValidatorException e) {
            if (e.getAnnotation() instanceof Logined) {
                ctx.status(401);
            } else {
                ctx.status(500);
                ctx.render(AnnoResult.failure(e.getCode(), e.getMessage()));
            }
        } catch (BizException e) {
            ctx.status(500);

            log.error(e.getMessage(), e);
            ctx.render(AnnoResult.failure(e.getMessage()));
        } catch (DateTimeParseException e) {
            log.error(e.getMessage(), e);

            ctx.status(500);
            ctx.render(AnnoResult.failure("日期格式化出错"));
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);

            ctx.status(400);
            ctx.render(AnnoResult.failure("非法参数"));
        } catch (SaTokenException e) {
            log.error(e.getMessage(), e);

            ctx.status(401);
            if (e instanceof NotPermissionException) {
                ctx.render(AnnoResult.failure("权限不足"));
                return;
            }
            ctx.render(AnnoResult.failure(e.getMessage()));
        } catch (Exception e) {
            log.error("未知异常 ==>", e);

            ctx.status(500);
            ctx.render(AnnoResult.failure("系统异常，请联系管理员"));
        }
    }
}