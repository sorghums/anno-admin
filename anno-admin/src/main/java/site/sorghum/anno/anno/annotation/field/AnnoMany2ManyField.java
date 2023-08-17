package site.sorghum.anno.anno.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AnnoMany2ManyField {
    /**
     * 中间表名称
     */
    String mediumTable();

    /**
     * 关联该表的列
     */
    AnnoJoinColumn thisColumn();

    /**
     * 关联另一表的列
     */
    AnnoJoinColumn otherColumn();
}