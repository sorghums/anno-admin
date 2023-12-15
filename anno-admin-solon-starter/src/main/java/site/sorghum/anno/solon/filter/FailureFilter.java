package site.sorghum.anno.solon.filter;


import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.hutool.core.text.AntPathMatcher;
import lombok.extern.slf4j.Slf4j;
import org.noear.dami.exception.DamiException;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Condition;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import org.noear.solon.validation.ValidatorException;
import org.noear.solon.validation.annotation.Logined;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.AnnoContextUtil;

import java.sql.SQLException;
import java.time.format.DateTimeParseException;

/**
 * 全局异常拦截
 *
 * @author Sorghum
 * @since 2023/02/24
 */
@Component(index = -10)
@Slf4j
@Condition(onProperty = "${anno-admin.class.FailureFilter:true} = true")
public class FailureFilter implements Filter {

    @Inject
    AnnoProperty annoProperty;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Init
    public void init () {
        antPathMatcher.setCachePatterns(true);
    }

    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        AnnoContextUtil.AnnoContext context = AnnoContextUtil.getContext();
        // 只处理anno-admin-api请求
        if (!ctx.path().startsWith(AnnoConstants.BASE_URL)) {
            chain.doFilter(ctx);
            return;
        }
        context.getStopWatch(ctx.path());

        context.setPrintDetailLog(annoProperty.getSkipPathPattern().stream().noneMatch(pattern -> antPathMatcher.match(pattern, ctx.path())));
        if (context.isPrintDetailLog()) {
            log.info("请求路径：{}", ctx.path());
        }
        try {
            try {
                chain.doFilter(ctx);
            }catch (DamiException damiException){
                throw damiException.getCause();
            }
        } catch (ValidatorException e) {
            if (e.getAnnotation() instanceof Logined) {
                ctx.render(AnnoResult.failure(401, e.getMessage()));
            } else {
                ctx.render(AnnoResult.failure(e.getCode(), e.getMessage()));
            }
        } catch (BizException e) {
            log.error(e.getMessage(), e);
            ctx.render(AnnoResult.failure(e.getMessage()));
        } catch (DateTimeParseException e) {
            log.error(e.getMessage(), e);
            ctx.render(AnnoResult.failure("日期格式化出错"));
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            ctx.render(AnnoResult.failure("非法参数,%s".formatted(e.getMessage())));
        } catch (SaTokenException e) {
            log.error(e.getMessage(), e);
            if (e instanceof NotPermissionException) {
                ctx.render(AnnoResult.failure(400,"权限不足"));
                return;
            }
            ctx.render(AnnoResult.failure(401,e.getMessage()));
        } catch (SQLException e) {
            log.error("数据库异常 ==>", e);
            ctx.render(AnnoResult.failure("数据库异常：%s".formatted(e.getMessage())));
        } catch (Exception e) {
            log.error("未知异常 ==>", e);
            ctx.render(AnnoResult.failure("系统异常，请联系管理员"));
        }
    }
}