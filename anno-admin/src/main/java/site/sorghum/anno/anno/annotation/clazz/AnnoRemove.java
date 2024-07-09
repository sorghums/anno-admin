package site.sorghum.anno.anno.annotation.clazz;

import java.lang.annotation.*;

/**
 * Anno 删除配置
 * <p>
 * 可配置逻辑删除
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface AnnoRemove {

    int DEFAULT_REMOVE_TYPE = 0;
    /**
     * 删除类型 0 物理删除 1 逻辑删除
     *
     * @return int
     */
    int removeType() default DEFAULT_REMOVE_TYPE;

    String DEFAULT_REMOVE_VALUE = "1";

    /**
     * 逻辑删除值
     * 默认 1
     *
     * @return {@link String}
     */
    String removeValue() default DEFAULT_REMOVE_VALUE;

    String DEFAULT_NOT_REMOVE_VALUE = "0";

    /**
     * 逻辑删除值
     * 默认 0
     *
     * @return {@link String}
     */
    String notRemoveValue() default DEFAULT_NOT_REMOVE_VALUE;

    String DEFAULT_REMOVE_FIELD = "del_flag";

    /**
     * 逻辑删除字段
     * 默认 del_flag
     *
     * @return {@link String}
     */
    String removeField() default DEFAULT_REMOVE_FIELD;
}
