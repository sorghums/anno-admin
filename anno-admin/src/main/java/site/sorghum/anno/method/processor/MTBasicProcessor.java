package site.sorghum.anno.method.processor;

import site.sorghum.anno.method.MTContext;
import site.sorghum.anno.method.MTProcessResult;
import site.sorghum.anno.method.MTProcessorInfo;

/**
 * 方法模版基类 处理器
 *
 * @author songyinyin
 * @since 2024/1/15 15:25
 */
public interface MTBasicProcessor {

    MTProcessResult process(MTContext context) throws Exception;

    default MTProcessorInfo getProcessorInfo() {
        return null;
    }
}
