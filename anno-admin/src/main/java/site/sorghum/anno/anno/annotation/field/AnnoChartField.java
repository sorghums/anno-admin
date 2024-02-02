package site.sorghum.anno.anno.annotation.field;

import site.sorghum.anno.anno.enums.AnnoChartType;

import java.lang.annotation.*;
import java.util.function.Supplier;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface AnnoChartField {

    String name() default "";

    AnnoChartType type() default AnnoChartType.NUMBER;

    Class<? extends Supplier<?>> runSupplier();

    int order() default 0;

    String permissionCode() default "";

    String icon() default "visit-count|svg";

    String color() default "blue";

    String action() default "日";
}
