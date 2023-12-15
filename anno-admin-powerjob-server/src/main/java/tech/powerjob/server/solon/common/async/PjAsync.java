package tech.powerjob.server.solon.common.async;

import java.lang.annotation.*;

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
