package site.sorghum.anno.cache;

import org.noear.solon.annotation.Note;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存移除注解器
 *
 * 注意：针对 Controller、Service、Dao 等所有基于MethodWrap运行的目标，才有效
 *
 * @author noear
 * @since 1.0
 * */
@Inherited //要可继承
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheRemove {
    /**
     * 缓存服务
     * */
    String service() default "";

    /**
     * 缓存唯一标识，多个以逗号隔开，例：user_${user_id}
     * */
    String keys() default "";

    /**
     * 清除缓存标签，多个以逗号隔开，例：user_${user_id} ，user_id 为参数
     * */
    String tags() default "";
}
