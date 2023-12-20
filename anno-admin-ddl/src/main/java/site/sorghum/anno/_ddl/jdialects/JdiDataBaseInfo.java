package site.sorghum.anno._ddl.jdialects;

import com.github.drinkjava2.jdialects.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * Jdi数据库上下文
 *
 * @author Sorghum
 * @since 2023/12/20
 */
public class JdiDataBaseInfo {
    /**
     * 本机类型
     */
    private static final Map<Integer, Type> NATIVE_TYPES = new HashMap<>();

    public static void addNativeTypeMapping(Integer jdbcTypeCode, Type nativeType) {
        NATIVE_TYPES.put(jdbcTypeCode, nativeType);
    }

    public static Type getNativeType(Integer jdbcTypeCode) {
        return NATIVE_TYPES.get(jdbcTypeCode);
    }


}
