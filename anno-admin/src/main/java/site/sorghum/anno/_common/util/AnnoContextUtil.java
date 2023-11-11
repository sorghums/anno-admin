package site.sorghum.anno._common.util;

import lombok.Data;
import org.slf4j.Logger;
import site.sorghum.anno.anno.dami.AllEntityProxy;
import site.sorghum.anno.anno.util.ReentrantStopWatch;

import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    private static final ThreadLocal<AnnoContext> CONTEXT_THREADLOCAL = new ThreadLocal<>();

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
     * 判断是否有上下文
     */
    public static boolean hasContext() {
        return CONTEXT_THREADLOCAL.get() != null;
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

        /**
         * 记录当前请求的执行时间
         */
        ReentrantStopWatch stopWatch;

        private boolean printDetailLog;

        public ReentrantStopWatch getStopWatch(String name) {
            if (this.stopWatch == null) {
                this.stopWatch = new ReentrantStopWatch(name == null ? "" : name);
            }
            return this.stopWatch;
        }
    }

    /**
     * 打印详细日志
     */
    public static void printLog(Logger log, long detailLogThreshold) {
        AnnoContext context = getContext();
        if (context.printDetailLog && context.stopWatch != null
            && context.stopWatch.getTotalTimeMillis() >= detailLogThreshold) {

            log.info("{}", context.stopWatch.prettyPrint(TimeUnit.MILLISECONDS, taskInfo -> {
                if (taskInfo.getTaskName().contains(AllEntityProxy.class.getName())) {
                    return true;
                }
                return false;
            }));
        }
    }
}
