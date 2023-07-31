package site.sorghum.anno.anno.annotation.clazz;

import site.sorghum.anno.anno.proxy.AnnoPreDefaultProxy;
import site.sorghum.anno.anno.proxy.AnnoPreBaseProxy;

import java.lang.annotation.*;

/**
 * Anno代理
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface AnnoPreProxy {

    /**
     * 代理类
     *
     * @return {@link Class}<{@link ?} {@link extends} {@link AnnoPreBaseProxy}>
     */
    Class<? extends AnnoPreBaseProxy> value() default AnnoPreDefaultProxy.class;
}
