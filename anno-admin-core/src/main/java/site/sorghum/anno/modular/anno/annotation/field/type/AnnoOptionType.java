package site.sorghum.anno.modular.anno.annotation.field.type;

import java.lang.annotation.*;

/**
 * Anno 选择类型
 *
 * @author Sorghum
 * @since 2023/05/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface AnnoOptionType {
    /**
     * 选择数据
     *
     * @return {@link OptionData[]}
     */
    OptionData[] value() default {};

    @interface OptionData {
        /**
         * 显示的标签
         */
        String label();

        /**
         * 显示的值
         */
        String value();
    }
}
