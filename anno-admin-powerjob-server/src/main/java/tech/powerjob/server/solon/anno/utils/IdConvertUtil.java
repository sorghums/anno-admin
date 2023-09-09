package tech.powerjob.server.solon.anno.utils;

import cn.hutool.core.util.StrUtil;

/**
 * @author songyinyin
 * @since 2023/9/7 14:22
 */
public class IdConvertUtil {

    public static Long toLong(String id) {
        if (StrUtil.isBlank(id)) {
            return null;
        }
        return Long.valueOf(id);
    }

    public static String toString(Long id) {
        if (id == null) {
            return null;
        }
        return id.toString();
    }
}
