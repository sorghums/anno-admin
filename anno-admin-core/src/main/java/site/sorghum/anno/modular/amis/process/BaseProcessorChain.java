package site.sorghum.anno.modular.amis.process;

import org.noear.solon.core.handle.Context;
import site.sorghum.amis.entity.AmisBase;
import site.sorghum.amis.entity.AmisBaseWrapper;

import java.util.Map;

/**
 * 基础处理器
 *
 * @author Sorghum
 * @since 2023/07/07
 */
public interface BaseProcessorChain {
    /**
     * 继续下个处理 or 结束
     *
     * @param amisBaseWrapper   基础组件
     * @param clazz      类型
     * @param properties 属性
     */
    void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String,Object> properties);
}
