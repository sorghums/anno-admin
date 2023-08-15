package site.sorghum.anno.amis.process.processer;

import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno.amis.process.BaseProcessor;
import site.sorghum.anno.amis.process.BaseProcessorChain;
import site.sorghum.anno.amis.process.processer.treem2m.TreeM2mViewInitProcessor;

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
            AnnoBeanUtils.getBean(PROCESSORS.get(index.getAndIncrement())).doProcessor(amisBaseWrapper, clazz, properties, this);
        }
    }
}
