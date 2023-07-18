package site.sorghum.anno.common.util;

import lombok.Data;

import java.util.Map;

/**
 * AnnoContext上下文工具
 *
 * @author sorghum
 * @since 2023/07/15
 */
public class AnnoContextUtil {
    /**
     * 当前请求的上下文
     */
    private static ThreadLocal<AnnoContext> CONTEXT_THREADLOCAL = new ThreadLocal<>();

    /**
     * 获取当前请求的上下文
     *
     * @return {@link AnnoContext}
     */
    public static AnnoContext getContext() {
        if (CONTEXT_THREADLOCAL.get() == null) {
            CONTEXT_THREADLOCAL.set(new AnnoContext());
        }
        return CONTEXT_THREADLOCAL.get();
    }

    /**
     * 清除当前请求的上下文
     */
    public static void clearContext() {
        CONTEXT_THREADLOCAL.remove();
    }

    @Data
    public static class AnnoContext {
        /**
         * 当前请求的请求参数
         */
        Map<String, Object> requestParams;
    }
}
