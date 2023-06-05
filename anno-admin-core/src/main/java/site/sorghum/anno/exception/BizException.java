package site.sorghum.anno.exception;

/**
 * 业务异常
 *
 * @author Sorghum
 * @since 2023/02/23
 */
public class BizException extends RuntimeException{
    public BizException() {
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static BizException wrap(Throwable e) {
        if (e instanceof BizException) {
            return (BizException) e;
        } else {
            return new BizException(e);
        }
    }
}
