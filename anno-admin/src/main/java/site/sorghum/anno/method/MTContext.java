package site.sorghum.anno.method;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
 * @author songyinyin
 * @since 2024/1/16 17:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MTContext {

    private Method method;

    private Object[] args;

    public static MTContext of(Method method, Object... args) {
        return new MTContext(method, args);
    }
}
