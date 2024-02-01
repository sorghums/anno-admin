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
public class SnowIdSupplier implements FieldBaseSupplier<String> {
    @Override
    public String get() {
        return IdUtil.getSnowflakeNextIdStr();
    }
}
