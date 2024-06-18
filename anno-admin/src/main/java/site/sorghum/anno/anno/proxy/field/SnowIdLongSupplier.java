package site.sorghum.anno.anno.proxy.field;

import cn.hutool.core.util.IdUtil;
import jakarta.inject.Named;


/**
 * 雪花id供应商
 *
 * @author Sorghum
 * @since 2024/01/30
 */
@Named
public class SnowIdLongSupplier implements FieldBaseSupplier<Long> {
    @Override
    public Long get() {
        return IdUtil.getSnowflakeNextId();
    }
}
