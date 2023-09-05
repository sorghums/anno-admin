package site.sorghum.anno.spring.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.SaTokenException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._common.response.AnnoResult;

import java.time.format.DateTimeParseException;

/**
 * 全局异常拦截
 *
 * @author songyinyin
 * @since 2023/8/4 11:46
 */
@Slf4j
@Order(1000)
@RestControllerAdvice(basePackages = AnnoConfig.ANNO_BASE_PACKAGE)
public class GlobalExceptionHandler {


    @ExceptionHandler(BizException.class)
    public AnnoResult bizException(HttpServletResponse response, BizException ex) {
        log.error(ex.getMessage(), ex);
        setHttpStatus(() -> response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        return AnnoResult.failure(ex.getMessage());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public AnnoResult dateTimeParseException(HttpServletResponse response, DateTimeParseException e) {
        log.error(e.getMessage(), e);
        setHttpStatus(() -> response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        return AnnoResult.failure("日期格式化出错");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public AnnoResult illegalArgumentException(HttpServletResponse response, IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        setHttpStatus(() -> response.setStatus(HttpStatus.BAD_REQUEST.value()));
        return AnnoResult.failure("非法参数");
    }

    @ExceptionHandler(NotPermissionException.class)
    public AnnoResult notPermissionException(HttpServletResponse response, NotPermissionException e) {
        log.error(e.getMessage(), e);
        setHttpStatus(() -> response.setStatus(HttpStatus.UNAUTHORIZED.value()));
        return AnnoResult.failure("权限不足");
    }

    @ExceptionHandler(SaTokenException.class)
    public AnnoResult saTokenException(HttpServletResponse response, SaTokenException e) {
        log.error(e.getMessage(), e);
        setHttpStatus(() -> response.setStatus(HttpStatus.UNAUTHORIZED.value()));
        return AnnoResult.failure(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public AnnoResult exception(HttpServletResponse response, Exception e) {
        log.error(e.getMessage(), e);
        setHttpStatus(() -> response.setStatus(HttpStatus.UNAUTHORIZED.value()));
        return AnnoResult.failure("系统异常，请联系管理员 " + e.getMessage());
    }

    private void setHttpStatus(Runnable runnable) {
        String url = SaHolder.getRequest().getUrl();
        if (!url.contains("/amis/")){
            runnable.run();
        };
    }
}
