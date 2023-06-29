package site.sorghum.anno.util;


import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;

import java.nio.charset.Charset;

/**
 * 加密工具类
 *
 * @author Sorghum
 * @since 2023/06/27
 */
public class CryptoUtil {
    public static SM2 sm2 = SmUtil.sm2();

    public synchronized static String encrypt(String str) {
        return Base64.encodeStr(sm2.encrypt(str.getBytes(Charset.defaultCharset())),false,false);
    }

    public synchronized static String decrypt(String str) {
        return new String(sm2.decrypt(Base64.decode(str)));
    }
}
