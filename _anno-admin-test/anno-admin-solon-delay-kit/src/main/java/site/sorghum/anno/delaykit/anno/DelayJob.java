package site.sorghum.anno.delaykit.anno;

import java.lang.annotation.*;

/**
 * DelayJob注解
 * 用于标注注册到delay-job的注解
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Deprecated
public @interface DelayJob {

    /**
     * 名称
     *
     * @return {@link String}
     */
    String name();

}
