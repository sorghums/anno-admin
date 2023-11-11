package site.sorghum.anno.spring.filter;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.RequestFacade;
import org.springframework.context.annotation.Configuration;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._common.util.AnnoContextUtil;
import site.sorghum.anno._common.util.JSONUtil;
import site.sorghum.anno._common.util.ThrowableLogUtil;
import site.sorghum.anno.anno.util.ReentrantStopWatch;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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

    @Inject
    AnnoProperty annoProperty;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String extraData = null;
        HashMap<String, Object> bdMap;
        if (!(servletRequest instanceof HttpServletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
        AnnoContextUtil.AnnoContext context = AnnoContextUtil.getContext();
        if (servletRequest instanceof RequestFacade) {
            String requestURI = ((RequestFacade) servletRequest).getRequestURI();
            context.getStopWatch(requestURI);

            context.setPrintDetailLog(annoProperty.getSkipPathPattern().stream().noneMatch(pattern -> antPathMatcher.match(pattern, requestURI)));
        }


        try {
            // 如果是文件上传则不进行处理
            if (servletRequest.getContentType() != null && servletRequest.getContentType().startsWith("multipart/form-data")) {
                filterChain.doFilter(servletRequest, servletResponse);
                // 清除请求上下文
                AnnoContextUtil.clearContext();
                return;
            }
            RequestWrapper requestWrapper = new RequestWrapper(servletRequest);
            String body = requestWrapper.getBody();
            if (body.startsWith("{")) {
                bdMap = JSONUtil.toBean(body, HashMap.class);
                if (bdMap.containsKey("_extraData")) {
                    extraData = MapUtil.getStr(bdMap, EXTRA_DATA);
                }
            } else if (StrUtil.isBlank(body)) {
                bdMap = new HashMap<>();
            } else {
                bdMap = null;
            }
            if (StrUtil.isNotBlank(requestWrapper.getParameter(EXTRA_DATA))) {
                extraData = requestWrapper.getParameter(EXTRA_DATA);
            }
            if (bdMap != null && StrUtil.isNotBlank(extraData)) {
                try {
                    HashMap<String, Object> param = JSONUtil.toBean(extraData, HashMap.class);
                    param.forEach(
                        (k, v) -> {
                            if (ObjUtil.isNotEmpty(v)) {
                                requestWrapper.paramMap.put(k, new String[]{v.toString()});
                                bdMap.put(k, v);
                            }
                        }
                    );
                    bdMap.forEach((k, v) -> {
                        if (ObjUtil.isNotEmpty(v)) {
                            requestWrapper.paramMap.put(k, new String[]{v.toString()});
                            bdMap.put(k, v);
                        }
                    });
                    requestWrapper.setBody(JSONUtil.toJsonString(bdMap));
                } catch (Exception e) {
                    ThrowableLogUtil.error(e);
                }
            }
            AnnoContextUtil.getContext().setRequestParams(bdMap);
            filterChain.doFilter(requestWrapper, servletResponse);
        } finally {
            // 清除请求上下文
            AnnoContextUtil.printLog(log, annoProperty.getDetailLogThreshold());
            AnnoContextUtil.clearContext();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}