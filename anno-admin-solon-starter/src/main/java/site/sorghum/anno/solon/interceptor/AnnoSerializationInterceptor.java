package site.sorghum.anno.solon.interceptor;

import org.noear.solon.core.aspect.Interceptor;
import org.noear.solon.core.aspect.Invocation;
import site.sorghum.anno._annotations.AnnoSerialization;
import site.sorghum.anno._common.response.AnnoResult;
import site.sorghum.anno._common.util.JSONUtil;

/**
 * anno序列化拦截器
 *
 * @author Sorghum
 * @since 2024/01/24
 */
public class AnnoSerializationInterceptor implements Interceptor {
    @Override
    public Object doIntercept(Invocation inv) throws Throwable {
        AnnoSerialization anno = inv.method().getAnnotation(AnnoSerialization.class);
        if (anno == null) {
            anno = inv.getTargetClz().getAnnotation(AnnoSerialization.class);
        }
        if (anno != null){
            Object invoke = inv.invoke();
            if (invoke instanceof AnnoResult<?> annoResult){
                return JSONUtil.toJsonString(annoResult);
            }
            return invoke;
        }else{
            return inv.invoke();
        }
    }
}