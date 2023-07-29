package site.sorghum.anno.modular.anno.annotation.clazz;

import site.sorghum.anno.modular.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.modular.anno.proxy.AnnoDefaultProxy;

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
public @interface AnnoProxy {

    /**
     * 代理类
     *
     * @return {@link Class<? extends AnnoBaseProxy<?>>}
     */
    Class<? extends AnnoBaseProxy> value() default AnnoDefaultProxy.class;
}
