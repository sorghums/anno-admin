package site.sorghum.anno.spring.filter;


import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.SaTokenException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.AnnoContextUtil;
import site.sorghum.anno._common.util.ThrowableLogUtil;

import java.io.IOException;
import java.time.format.DateTimeParseException;

/**
 * 全局异常拦截
 *
 * @author Sorghum
 * @since 2023/02/24
 */
@Configuration
@WebFilter(urlPatterns = "/*",filterName = "extraDataFilter")
@Slf4j
public class FailureFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest,servletResponse);
            // 请求完成后自动清除上下文
            AnnoContextUtil.clearContext();
        } catch (BizException e){
            ThrowableLogUtil.error(e);
            servletResponse.getOutputStream().print(AnnoResult.failure(e.getMessage()).toString());
        } catch (DateTimeParseException e){
            ThrowableLogUtil.error(e);
            servletResponse.getOutputStream().print(AnnoResult.failure("日期格式化出错").toString());
        } catch (IllegalArgumentException e){
            ThrowableLogUtil.error(e);
            log.error("参数错误 ==>",e);
            servletResponse.getOutputStream().print(AnnoResult.failure( "非法参数").toString());
        } catch (SaTokenException e){
            ThrowableLogUtil.error(e);
            if (e instanceof NotPermissionException){
                servletResponse.getOutputStream().print(AnnoResult.failure("权限不足").toString());
                return;
            }
            servletResponse.getOutputStream().print(AnnoResult.failure(e.getMessage()).toString());
        }catch (Exception e){
            e.printStackTrace();
            log.error("未知异常 ==>",e);
            ThrowableLogUtil.error(e);
            servletResponse.getOutputStream().write(AnnoResult.failure("系统异常，请联系管理员").toString().getBytes());
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}