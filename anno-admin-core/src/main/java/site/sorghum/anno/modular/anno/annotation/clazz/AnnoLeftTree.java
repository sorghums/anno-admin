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
public @interface AnnoLeftTree {

    /**
     * 分类关键词
     * 查询的时候会用到
     *
     * @return {@link String}
     */
    String catKey();

    /**
     * 树类
     *
     * @return {@link Class}<{@link ?}>
     */
    Class<?> treeClass();

    /**
     * 是否启用
     *
     * @return boolean
     */
    boolean enable() default true;
}
