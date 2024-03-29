package site.sorghum.anno.db.exception;

/**
 * Anno数据库异常
 *
 * @author sorghum
 * @since 2023/07/08
 */
public class AnnoDbException extends RuntimeException {
    public AnnoDbException(String message) {
        super(message);
    }

    public AnnoDbException withCause(Throwable cause){
        initCause(cause);
        return this;
    }
}
