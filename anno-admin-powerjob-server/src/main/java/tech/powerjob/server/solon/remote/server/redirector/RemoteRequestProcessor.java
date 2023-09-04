package tech.powerjob.server.solon.remote.server.redirector;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson2.JSONObject;
import org.noear.solon.Solon;

import java.lang.reflect.Method;

/**
 * process remote request
 *
 * @author tjq
 * @since 2021/2/19
 */
public class RemoteRequestProcessor {

    public static Object processRemoteRequest(RemoteProcessReq req) throws ClassNotFoundException {
        Object[] args = req.getArgs();
        String[] parameterTypes = req.getParameterTypes();
        Class<?>[] parameters = new Class[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = Class.forName(parameterTypes[i]);
            Object arg = args[i];
            if (arg != null) {
                args[i] = JSONObject.parseObject(JSONObject.toJSONString(arg), parameters[i]);
            }
        }

        Class<?> clz = Class.forName(req.getClassName());

        Object bean = Solon.context().getBean(clz);
        Method method = ReflectUtil.getMethod(clz, req.getMethodName(), parameters);

        assert method != null;
        return ReflectUtil.invoke(bean, method, args);
    }
}
