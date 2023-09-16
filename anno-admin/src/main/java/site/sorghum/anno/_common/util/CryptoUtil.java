package site.sorghum.anno._common.util;


import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;

import javax.crypto.SecretKey;
import java.nio.charset.Charset;

/**
 * 加密工具类
 *
 * @author Sorghum
 * @since 2023/06/27
 */
public class CryptoUtil {

    static AES aes;

    static {
        SecretKey aesKey = SecureUtil.generateKey("AES");
        aes = SecureUtil.aes(aesKey.getEncoded());
    }

    public static String encrypt(String str) {
        return Base64.encodeStr(aes.encrypt(str.getBytes(Charset.defaultCharset())),false,false);
    }

    public static String decrypt(String str) {
        return new String(aes.decrypt(Base64.decode(str)));
    }
}
