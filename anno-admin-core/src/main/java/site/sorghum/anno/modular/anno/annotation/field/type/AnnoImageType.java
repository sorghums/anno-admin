package site.sorghum.anno.modular.anno.annotation.field.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.*;

/**
 * Anno庵野图像类型
 *
 * @author sorghum
 * @since 2023/05/27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface AnnoImageType {
    /**
     * 预览图比率
     *
     * @return {@link String}
     */
    ThumbRatio thumbRatio() default ThumbRatio.RATE_ONE;


    /**
     * 图片模式
     *
     * @return {@link ThumbMode}
     */
    ThumbMode thumbMode() default ThumbMode.DEFAULT;

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
    int width() default 0;

    /**
     * 高度 px
     *
     * @return int
     */
    int height() default 0;


    /**
     * 预览图比率
     *
     * @author sorghum
     * @since 2023/05/27
     */
    @AllArgsConstructor
    @Getter
    enum ThumbRatio {
        RATE_ONE("1:1"), RATE_TWO("4:3"), RATE_THREE("16:9");

        private String ratio;
    }

    /**
     * 图片模式
     *
     * @author sorghum
     * @since 2023/05/27
     */
    @AllArgsConstructor
    @Getter
    enum ThumbMode {

        /**
         * 宽度占满
         */
        W_FULL("w-full"),
        /**
         * 高度占满
         */
        H_FULL("h-full"),
        /**
         * 默认
         */
        DEFAULT("contain"),
        /**
         * 覆盖
         */
        COVER("cover");

        /**
         * 模式
         */
        private String mode;
    }
}
