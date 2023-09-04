package tech.powerjob.server.solon.common.async;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 异步运行
 *
 * @author songyinyin
 * @since 2023/8/27 15:32
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PjAsync {

    /**
     * 线程池名称
     */
    String value() default "";
}
