package site.sorghum.anno.method;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author songyinyin
 * @since 2024/1/16 17:17
 */
@Data
@AllArgsConstructor
public class MTProcessResult {

    private boolean success;

    private Object result;

    public static MTProcessResult success(Object result) {
        return new MTProcessResult(true, result);
    }

    public static MTProcessResult success() {
        return new MTProcessResult(true, null);
    }

    public static MTProcessResult fail(Object result) {
        return new MTProcessResult(false, result);
    }
}
