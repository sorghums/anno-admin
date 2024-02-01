package site.sorghum.anno.spring.filter;


import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.hutool.core.exceptions.InvocationTargetRuntimeException;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.text.AntPathMatcher;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.RequestFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.AnnoContextUtil;
import site.sorghum.anno._common.util.JSONUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;

/**
 * 额外数据重新设置
 *
 * @author Sorghum
 * @since 2023/02/24
 */
@Configuration
@WebFilter(urlPatterns = AnnoConstants.BASE_URL + "/*", filterName = "extraDataFilter")
@Slf4j
public class ExtraDataFilter implements Filter {
    private static final String EXTRA_DATA = "_extraData";

    @Autowired
    AnnoProperty annoProperty;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            try {
                if (!(servletRequest instanceof HttpServletRequest)) {
                    filterChain.doFilter(servletRequest, servletResponse);
                }
                AnnoContextUtil.AnnoContext context = AnnoContextUtil.getContext();
                if (servletRequest instanceof RequestFacade) {
                    String requestUri = ((RequestFacade) servletRequest).getRequestURI();
                    context.getStopWatch(requestUri);
                    context.setPrintDetailLog(annoProperty.getSkipPathPattern().stream().noneMatch(pattern -> antPathMatcher.match(pattern, requestUri)));
                }
                filterChain.doFilter(servletRequest, servletResponse);
            } catch (UtilException | ServletException exception) {
                throw processThrowable(exception, null);
            }
        } catch (BizException e) {
            log.error(e.getMessage(), e);
            writeResponse(servletResponse, AnnoResult.failure(e.getMessage()));
        } catch (DateTimeParseException e) {
            log.error(e.getMessage(), e);
            writeResponse(servletResponse, AnnoResult.failure("日期格式化出错"));
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            writeResponse(servletResponse, AnnoResult.failure("非法参数,%s".formatted(e.getMessage())));
        } catch (SaTokenException e) {
            log.error(e.getMessage(), e);
            if (e instanceof NotPermissionException) {
                writeResponse(servletResponse, AnnoResult.failure(400, "权限不足"));
                return;
            }
            writeResponse(servletResponse, AnnoResult.failure(401, e.getMessage()));
        } catch (SQLException e) {
            log.error("数据库异常 ==>", e);
            writeResponse(servletResponse, AnnoResult.failure("数据库异常：%s".formatted(e.getMessage())));
        } catch (Throwable e) {
            log.error("未知异常 ==>", e);
            writeResponse(servletResponse, AnnoResult.failure("系统异常，请联系管理员"));
        } finally {
            AnnoContextUtil.printLog(log, annoProperty.getDetailLogThreshold());
            // 清除请求上下文
            AnnoContextUtil.clearContext();
        }
    }

    private void writeResponse(ServletResponse response, AnnoResult<?> result) throws IOException {
        if (result == null) {
            return;
        }
        if (response instanceof HttpServletResponse httpServletResponse) {
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.getWriter().print(JSONUtil.toJsonString(result));
        }
    }

    private Throwable processThrowable(Throwable t, Throwable parent) {
        if (t == null) {
            return parent;
        }
        if (t == parent) {
            return t;
        }
        if (t instanceof UtilException utilException) {
            return processThrowable(utilException.getCause(), utilException);
        } else if (t instanceof ServletException servletException) {
            return processThrowable(servletException.getRootCause(), servletException);
        } else if (t instanceof InvocationTargetException invocationTargetException) {
            return processThrowable(invocationTargetException.getCause(), invocationTargetException);
        } else {
            return t;
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}