package site.sorghum.anno.method.processor;

import cn.hutool.core.util.ClassUtil;
import lombok.Getter;
import site.sorghum.anno.method.ExecutePhase;
import site.sorghum.anno.method.MTContext;
import site.sorghum.anno.method.MTException;
import site.sorghum.anno.method.MTProcessResult;
import site.sorghum.anno.method.MTProcessorInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author songyinyin
 * @since 2024/1/16 17:21
 */
public class MethodBasicProcessor implements MTBasicProcessor {

    private final Object bean;

    /**
     * method to be invoked, not null
     */
    @Getter
    private Method method;

    private final MTProcessorInfo processorInfo;

    public MethodBasicProcessor(Object bean, String methodName, MTProcessorInfo processorInfo) {
        this.bean = bean;
        this.processorInfo = processorInfo;

        Method[] methods = ClassUtil.getPublicMethods(bean.getClass());
        List<Method> methodList = new ArrayList<>();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && !method.isBridge()) {
                methodList.add(method);
            }
        }
        if (methodList.size() > 1) {
            throw new MTException("%s has more than one method named: %s".formatted(bean.getClass().getName(), methodName));
        }
        if (methodList.size() == 1) {
            this.method = methodList.get(0);
        }
        if (this.method == null) {
            throw new MTException("%s not found method: %s".formatted(bean.getClass().getName(), methodName));
        }
    }

    @Override
    public MTProcessResult process(MTContext context) throws Exception {
        try {
            if (bean instanceof MTBasicProcessor basicProcessor) {
                return basicProcessor.process(context);
            }
            Object result = method.invoke(bean, context.getArgs());
            return new MTProcessResult(true, result);
        } catch (InvocationTargetException ite) {
            throw (Exception) ite.getTargetException();
        }
    }

    @Override
    public MTProcessorInfo getProcessorInfo() {
        return processorInfo;
    }

    public double getIndex() {
        return processorInfo.getIndex();
    }

    public double getPhaseOrdinal() {
        if (processorInfo.getPhase() == null) {
            return ExecutePhase.EXECUTE.ordinal();
        }
        return processorInfo.getPhase().ordinal();
    }
}
