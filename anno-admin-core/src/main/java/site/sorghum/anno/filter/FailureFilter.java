package site.sorghum.anno.filter;


import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.SaTokenException;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import org.noear.solon.validation.ValidatorException;
import org.noear.solon.validation.annotation.Logined;
import site.sorghum.anno.exception.BizException;
import site.sorghum.anno.response.AnnoResult;
import site.sorghum.anno.util.ThrowableLogUtil;

import java.time.format.DateTimeParseException;

/**
 *故障过滤
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
        } catch (ValidatorException e) {
            if(e.getAnnotation() instanceof Logined){
                ctx.status(401);
            }else {
                ctx.render(AnnoResult.failure(e.getCode(), e.getMessage()));
            }
        } catch (BizException e){
            ThrowableLogUtil.error(e);
            ctx.render(AnnoResult.failure(e.getMessage()));
        } catch (DateTimeParseException e){
            ThrowableLogUtil.error(e);
            ctx.render(AnnoResult.failure("日期格式化出错"));
        } catch (IllegalArgumentException e){
            ThrowableLogUtil.error(e);
            e.printStackTrace();
            ctx.render(AnnoResult.failure( "非法参数"));
        } catch (SaTokenException e){
            ThrowableLogUtil.error(e);
            if (e instanceof NotPermissionException){
                ctx.render(AnnoResult.failure("权限不足"));
                return;
            }
            ctx.render(AnnoResult.failure(e.getMessage()));
        }catch (Exception e){
            log.error("未知异常 ==>",e);
            ThrowableLogUtil.error(e);
            ctx.render(AnnoResult.failure("系统异常，请联系管理员"));
        }
    }
}