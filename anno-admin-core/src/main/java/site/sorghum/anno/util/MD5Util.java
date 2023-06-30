package site.sorghum.anno.util;

import cn.hutool.crypto.digest.MD5;

/**
 * MD5工具类
 * @author Sorghum
 * @since 2023/06/30
 */
public class MD5Util {
    /**
     * Md5实例
     */
    private final static MD5 MD5_INSTANCE = MD5.create();

    public static String digestHex(String str) {
        return MD5_INSTANCE.digestHex(str);
    }
}
