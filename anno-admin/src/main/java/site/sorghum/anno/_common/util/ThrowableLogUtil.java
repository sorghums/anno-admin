package site.sorghum.anno._common.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * throwable日志打印工具
 *
 * @author sorghum
 * @since 2022/06/29
 */
@Slf4j
public class ThrowableLogUtil {
    /**
     * 调用栈偏移量
     */
    private static final int OFFSET = 2;
    /**
     * 基础包名
     */
    private static final String BASE_PACKAGE = "site.sorghum";

    /**
     * 打印throwable的日志
     *
     * @param inThrowable 在throwable
     */
    public static void error(Throwable inThrowable,int offset) {
        StackTraceElement[] nowStack = Thread.currentThread().getStackTrace();
        StackTraceElement rootStackElement = new StackTraceElement("未知类", "未知方法", "未知文件名", 0);
        if (nowStack.length >= OFFSET + offset) {
            rootStackElement = nowStack[offset+OFFSET];
        }
        Throwable throwable = getRootCause(inThrowable);
        if (!Objects.isNull(throwable)){
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            StackTraceElement selfStack = getSelfStack(stackTrace);
            if (!Objects.isNull(selfStack)) {
                log.error("异常信息:{} 出错类名：{},出错方法名：{},行号：{}", getMessage(throwable),selfStack.getClassName(), selfStack.getMethodName(), selfStack.getLineNumber());
            }else {
                log.error("[{}] 行号：{},异常信息：{}",rootStackElement.getMethodName(),rootStackElement.getLineNumber(), getMessage(throwable));
            }
        }else {
            log.error("[{}] 行号：{},打印错误异常：{}，获取源信息失败。", rootStackElement.getMethodName(), rootStackElement.getLineNumber(), inThrowable.getMessage());
        }

    }
    /**
     * 打印throwable的日志
     *
     * @param inThrowable 在throwable
     */
    public static void error(Throwable inThrowable) {
        error(inThrowable,1);
    }

    /**
     * 获得调用栈
     *
     * @param stackTrace 堆栈跟踪
     * @return {@link StackTraceElement}
     */
    private static StackTraceElement getSelfStack(StackTraceElement[] stackTrace) {
        if (Objects.isNull(stackTrace) || stackTrace.length == 0) {
            return null;
        }
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (stackTraceElement.getClassName().contains(BASE_PACKAGE)) {
                return stackTraceElement;
            }
        }
        return null;
    }


    /**
     * 得到调用跟踪的根异常
     *
     * @param throwable throwable
     * @return {@link Throwable}
     */
    private static Throwable getRootCause(Throwable throwable) {
        Throwable dummyThrowable = new Throwable(throwable);
        while (dummyThrowable.getCause() != null && dummyThrowable.getCause() != dummyThrowable){
            dummyThrowable = dummyThrowable.getCause();
        }
        return dummyThrowable;
    }

    /**
     * 获取异常信息
     *
     * @param e e
     * @return {@link String}
     */
    private static String getMessage(Throwable e) {
        if (null == e) {
            return StrUtil.NULL;
        }
        return StrUtil.format("{}: {}", e.getClass().getSimpleName(), e.getMessage());
    }

}
