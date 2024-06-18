package site.sorghum.anno.anno.proxy.field;

import jakarta.inject.Named;

@Named
public class ZeroFiledStringBaseSupplier implements FieldBaseSupplier<String> {
    @Override
    public String get() {
        return "0";
    }
}
