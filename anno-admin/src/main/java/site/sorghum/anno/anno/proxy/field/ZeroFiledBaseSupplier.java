package site.sorghum.anno.anno.proxy.field;

import jakarta.inject.Named;

@Named
public class ZeroFiledBaseSupplier implements FieldBaseSupplier<Integer> {
    @Override
    public Integer get() {
        return 0;
    }
}
