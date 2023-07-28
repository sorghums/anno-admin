package site.sorghum.anno.modular.amis.process;

import site.sorghum.amis.entity.AmisBaseWrapper;

import java.util.Map;

/**
 * 基础处理器
 *
 * @author Sorghum
 * @since 2023/07/07
 */
public interface BaseProcessor {
    /**
     * 处理
     *
     * @param amisBaseWrapper   基础组件
     * @param clazz      类型
     * @param properties 属性
     * @throws Throwable throwable
     */
    void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String,Object> properties, BaseProcessorChain chain);
}
