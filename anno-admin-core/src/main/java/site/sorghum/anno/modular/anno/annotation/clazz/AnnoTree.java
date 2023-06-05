package site.sorghum.anno.modular.anno.annotation.clazz;

import java.lang.annotation.*;

/**
 * Anno 左树
 *
 * @author sorghum
 * @since 2023/05/21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface AnnoTree {
    /**
     * 标签
     *
     * @return {@link String}
     */
    String label();

    /**
     * 父节点关键词
     *
     * @return {@link String}
     */
    String parentKey();

    /**
     * 节点关键词
     *
     * @return {@link String}
     */
    String key();

    /**
     * 是否显示为树
     * @return boolean
     */
    boolean displayAsTree();

    /**
     * 是否启用
     *
     * @return boolean
     */
    boolean enable() default true;
}
