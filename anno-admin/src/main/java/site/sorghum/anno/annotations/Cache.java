package site.sorghum.anno.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存注解器
 *
 * 注意：针对 Controller、Service、Dao 等所有基于MethodWrap运行的目标，才有效
 *
 * @author noear
 * @since 1.0
 * */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    /**
     * 缓存服务
     * */
    String service() default "";

    /**
     * 0表示采用cache service的默认是境
     * */
    int seconds() default 0;

    /**
     * 缓存唯一标识，不能有逗号，例：user_${user_id}
     * */
    String key() default "";

    /**
     * 缓存标签，多个以逗号隔开，例：user_${user_id} ，user_id 为参数
     * */
    String tags() default "";
}
