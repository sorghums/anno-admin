package site.sorghum.anno.anno.annotation.clazz;

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
     * 左树名称
     *
     * @return {@link String}
     */
    String leftTreeName() default "左树";

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
