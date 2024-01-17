package site.sorghum.anno.method;

import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._common.util.AnnoContextUtil;
import site.sorghum.anno.anno.util.ReentrantStopWatch;
import site.sorghum.anno.method.processor.MTBasicProcessor;
import site.sorghum.anno.method.processor.MethodBasicProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author songyinyin
 * @since 2024/1/16 12:09
 */
@Slf4j
public class MethodTemplateInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("{}#{}() is invoke, args: {}", method.getDeclaringClass().getName(), method.getName(), args);
        }
        if (!MethodTemplateManager.isSupportMethod(method)) {
            throw new MTException("is not a support MethodTemplate method" + method.getName());
        }
        ReentrantStopWatch stopWatch;
        if (AnnoContextUtil.hasContext()) {
            //开始监视...
            stopWatch = AnnoContextUtil.getContext().getStopWatch();

        } else {
            stopWatch = new ReentrantStopWatch(getMethodName(method));
        }

        MTContext mtContext = MTContext.of(method, args);
        List<MTBasicProcessor> processors = MethodTemplateManager.getMethodProcessors(mtContext);
        Object result = null;
        for (MTBasicProcessor processor : processors) {
            String taskName;
            if (processor.getMethodTemplateCsv() == null) {
                taskName = processor.getClass().getSimpleName();
            } else {
                if (processor instanceof MethodBasicProcessor methodBasicProcessor) {
                    taskName = getMethodName(methodBasicProcessor.getMethod());
                } else {
                    taskName = processor.getMethodTemplateCsv().getBeanMethodName();
                }
            }

            stopWatch.start(taskName);

            MTProcessResult processResult = processor.process(mtContext);
            if (!processResult.isSuccess()) {
                break;
            }
            result = processResult.getResult();

            stopWatch.stop();
        }

        AnnoProperty annoProperty = AnnoBeanUtils.getBean(AnnoProperty.class);
        if (!AnnoContextUtil.hasContext() && stopWatch.getTotalTimeMillis() > annoProperty.getDetailLogThreshold()) {
            log.info("{}", stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        }
        return result;
    }

    private String getMethodName(Method method) {
        return "%s#%s()".formatted(method.getDeclaringClass().getName(), method.getName());
    }

}
