package site.sorghum.anno.spring.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.JSONUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Anno序列化切面
 *
 * @author Sorghum
 * @since 2024/01/24
 */
@Slf4j
@Aspect
@Component
public class AnnoSerializationAspect {
    /**
     * 定义切面Pointcut
     */
    @Pointcut(value = "@within(site.sorghum.anno._annotations.AnnoSerialization)")
    public void methodAnnoSerialization() {

    }

    @AfterReturning(value = "methodAnnoSerialization()", returning = "object")
    public Object doAfterReturning(JoinPoint joinPoint, Object object) {
        if(object instanceof AnnoResult annoResult){
            if (annoResult.getData() == null){
                return annoResult;
            }
            if (annoResult.getData() instanceof List){
                annoResult.setData(JSONUtil.toBeanList(annoResult.getData(), HashMap.class));
            }else {
                annoResult.setData(JSONUtil.toBean(annoResult.getData(), HashMap.class));
            }
        }
        return object;
    }

}