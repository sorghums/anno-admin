package site.sorghum.anno.modular.amis.process.processer;

import org.noear.solon.Solon;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;
import site.sorghum.anno.modular.amis.process.processer.crudm2m.*;
import site.sorghum.anno.modular.amis.process.processer.tree.TreeM2mViewInitProcessor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 树多对多处理器链
 *
 * @author Sorghum
 * @since 2023/07/07
 */
public class TreeM2mProcessorChain implements BaseProcessorChain {
    /**
     * 处理器索引
     */
    private final AtomicInteger index = new AtomicInteger(0);
    private static final List<Class<? extends BaseProcessor>> PROCESSORS = List.of(
            // 初始化
            TreeM2mViewInitProcessor.class
    );

    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties) {
        if (index.get() < PROCESSORS.size()) {
            Solon.context().getBean(PROCESSORS.get(index.getAndIncrement())).doProcessor(amisBaseWrapper, clazz, properties, this);
        }
    }
}
