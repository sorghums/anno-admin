package site.sorghum.anno.anno.annotation.field.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.*;

/**
 * Anno 图像类型
 *
 * @author sorghum
 * @since 2023/05/27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface AnnoImageType {
    /**
     * 点击可放大展示
     *
     * @return boolean
     */
    boolean enlargeAble() default true;

    /**
     * 宽度 px
     *
     * @return int
     */
    int width() default 50;

    /**
     * 高度 px
     *
     * @return int
     */
    int height() default 50;

}
