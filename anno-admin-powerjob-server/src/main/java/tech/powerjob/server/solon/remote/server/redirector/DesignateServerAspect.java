package tech.powerjob.server.solon.remote.server.redirector;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.aspect.Interceptor;
import org.noear.solon.core.aspect.Invocation;
import tech.powerjob.common.RemoteConstant;
import tech.powerjob.common.enums.Protocol;
import tech.powerjob.common.exception.PowerJobException;
import tech.powerjob.common.response.AskResponse;
import tech.powerjob.remote.framework.base.URL;
import tech.powerjob.server.solon.anno.utils.DbContextUtil;
import tech.powerjob.server.solon.persistence.remote.model.AppInfoDO;
import tech.powerjob.server.solon.persistence.remote.repository.AppInfoRepository;
import tech.powerjob.server.solon.remote.transporter.TransportService;
import tech.powerjob.server.solon.remote.transporter.impl.ServerURLFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

/**
 * 指定服务器运行切面
 *
 * @author tjq
 * @since 12/13/20
 */
@Slf4j
public class DesignateServerAspect implements Interceptor {

    private TransportService transportService;
    private AppInfoRepository appInfoRepository;

    public DesignateServerAspect(AppContext context) {
        context.getBeanAsync(TransportService.class, bean -> {
            this.transportService = bean;
        });
        this.appInfoRepository = DbContextUtil.getMapper(AppInfoRepository.class);
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public Object doIntercept(Invocation inv) throws Throwable {

        // 参数
        Map<String, Object> argsMap = inv.argsAsMap();
        // 方法名
        String methodName = inv.method().getMethod().getName();
        // 类名
        String className = inv.target().getClass().getSimpleName();

        DesignateServer designateServer = inv.method().getAnnotation(DesignateServer.class);

        Object appId = argsMap.get(designateServer.appIdParameterName());

        if (appId == null) {
            throw new PowerJobException("can't find appId in params for:" + argsMap.keySet());
        }

        // 获取执行机器
        AppInfoDO appInfo = appInfoRepository.selectById(appId);
        if (appInfo == null) {
            throw new PowerJobException("can't find app info");
        }
        String targetServer = appInfo.getCurrentServer();

        // 目标IP为空，本地执行
        if (StringUtils.isEmpty(targetServer)) {
            return inv.invoke();
        }

        // 目标IP与本地符合则本地执行
        if (Objects.equals(targetServer, transportService.defaultProtocol().getAddress())) {
            return inv.invoke();
        }

        log.info("[DesignateServerAspect] the method[{}] should execute in server[{}], so this request will be redirect to remote server!", String.format("%s#%s", className, methodName), targetServer);
        // 转发请求，远程执行后返回结果
        RemoteProcessReq remoteProcessReq = new RemoteProcessReq()
            .setClassName(className)
            .setMethodName(methodName)
            .setParameterTypes(Arrays.stream(inv.method().getParamWraps()).map(p -> p.getType().getName()).toArray(String[]::new))
            .setArgs(inv.args());

        final URL friendUrl = ServerURLFactory.process2Friend(targetServer);

        CompletionStage<AskResponse> askCS = transportService.ask(Protocol.HTTP.name(), friendUrl, remoteProcessReq, AskResponse.class);
        AskResponse askResponse = askCS.toCompletableFuture().get(RemoteConstant.DEFAULT_TIMEOUT_MS, TimeUnit.MILLISECONDS);

        if (!askResponse.isSuccess()) {
            throw new PowerJobException("remote process failed: " + askResponse.getMessage());
        }

        // 考虑范型情况
        JavaType returnType = getJavaType(inv.method().getReturnType());

        return OBJECT_MAPPER.readValue(askResponse.getData(), returnType);
    }

    private static JavaType getJavaType(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            Class<?> rowClass = (Class<?>) ((ParameterizedType) type).getRawType();
            JavaType[] javaTypes = new JavaType[actualTypeArguments.length];
            for (int i = 0; i < actualTypeArguments.length; i++) {
                //泛型也可能带有泛型，递归处理
                javaTypes[i] = getJavaType(actualTypeArguments[i]);
            }
            return TypeFactory.defaultInstance().constructParametricType(rowClass, javaTypes);
        } else {
            return TypeFactory.defaultInstance().constructParametricType((Class<?>) type, new JavaType[0]);
        }
    }


}
