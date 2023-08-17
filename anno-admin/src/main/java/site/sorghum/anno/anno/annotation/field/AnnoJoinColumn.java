package site.sorghum.anno.anno.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AnnoJoinColumn {
    /**
     * 中间表的列名称
     */
    String mediumName();

    /**
     * 引用表的列名称
     */
    String referencedName();
}
