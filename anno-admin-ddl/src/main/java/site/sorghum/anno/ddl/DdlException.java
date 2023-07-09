package site.sorghum.anno.ddl;

/**
 * @author songyinyin
 * @since 2023/7/2 16:41
 */
public class DdlException extends RuntimeException {
  public DdlException(String message) {
    super(message);
  }

  public DdlException(Throwable cause) {
    super(cause);
  }

  public DdlException(String message, Throwable cause) {
    super(message, cause);
  }
}
