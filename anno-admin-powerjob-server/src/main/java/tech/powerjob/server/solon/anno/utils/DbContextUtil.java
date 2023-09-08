package tech.powerjob.server.solon.anno.utils;

import org.noear.wood.DbContext;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno.suppose.mapper.AnnoBaseMapper;

/**
 * @author songyinyin
 * @since 2023/9/7 14:27
 */
public class DbContextUtil {

    /**
     * 从默认数据源获取 mapper
     */
    public static <T extends AnnoBaseMapper<?>> T getMapper(Class<T> mapperClass) {
        return DbContext.use(AnnoConstants.DEFAULT_DATASOURCE_NAME).mapper(mapperClass);
    }
}
