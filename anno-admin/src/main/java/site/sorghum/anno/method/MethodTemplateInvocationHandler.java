package site.sorghum.anno.method;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.googlecode.aviator.AviatorEvaluator;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._common.util.AnnoContextUtil;
import site.sorghum.anno.anno.util.ReentrantStopWatch;
import site.sorghum.anno.method.processor.MethodBasicProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            log.debug("{}#{}() is invoke, args size: {}", method.getDeclaringClass().getName(), method.getName(), args.length);
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
        List<MethodBasicProcessor> processors = MethodTemplateManager.getMethodProcessors(mtContext);
        if (CollUtil.isEmpty(processors)) {
            log.info("no processor found for method: {}#{}()", method.getDeclaringClass().getName(), method.getName());
            return null;
        }
        Object result = null;
        for (MethodBasicProcessor processor : processors) {
            MTProcessorInfo processorInfo = processor.getProcessorInfo();
            if (processorInfo != null && StrUtil.isNotBlank(processorInfo.getCondition())) {
                Map<String, Object> env = new HashMap<>();
                for (int i = 0; i < args.length; i++) {
                    env.put("p" + i, args[i]);
                }
                Object execute = AviatorEvaluator.getInstance().execute(processorInfo.getCondition(), env);
                // 如果不满足条件，不执行该部件
                if (!Boolean.TRUE.equals(execute)) {
                    continue;
                }
            }

            String taskName;
            if (processorInfo == null) {
                taskName = processor.getClass().getSimpleName();
            } else {
                taskName = getMethodName(processor.getMethod());
            }

            stopWatch.start("MT :: " + taskName);

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
