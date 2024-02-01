package site.sorghum.anno.anno.proxy.field;

import jakarta.inject.Named;

/**
 * 空字段基础供应商
 *
 * @author Sorghum
 * @since 2024/01/30
 */
@Named
public class EmptyFieldBaseSupplier implements FieldBaseSupplier<Object>{
    @Override
    public Object get() {
        return null;
    }
}
