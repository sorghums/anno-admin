package site.sorghum.anno.anno.interfaces;

import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.proxy.field.SnowIdSupplier;

public interface PrimaryKeyModelInterfaces {
    @AnnoField(title = "主键",
        tableFieldName = "id",
        show = false,
        fieldSize = 32,
        pkField = true,
        insertWhenNullSet = SnowIdSupplier.class)
    default void id(){};
}
