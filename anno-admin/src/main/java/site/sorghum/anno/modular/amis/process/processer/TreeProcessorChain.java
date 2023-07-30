package site.sorghum.anno.modular.amis.process.processer;

import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.anno.common.AnnoBeanUtils;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;
import site.sorghum.anno.modular.amis.process.processer.tree.TreeAsideProcessor;
import site.sorghum.anno.modular.amis.process.processer.tree.TreeBodyFormProcessor;
import site.sorghum.anno.modular.amis.process.processer.tree.TreeColumnButtonProcessor;
import site.sorghum.anno.modular.amis.process.processer.tree.TreeViewInitProcessor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 树处理器链
 *
 * @author Sorghum
 * @since 2023/07/07
 */
public class TreeProcessorChain implements BaseProcessorChain {
    /**
     * 处理器索引
     */
    private final AtomicInteger index = new AtomicInteger(0);
    private static final List<Class<? extends BaseProcessor>> PROCESSORS = List.of(
        // 树视图初始化处理器
        TreeViewInitProcessor.class,
        // 树表单处理器
        TreeBodyFormProcessor.class,
        // 树边栏处理器
        TreeAsideProcessor.class,
        // 树列按钮处理器
        TreeColumnButtonProcessor.class
    );

    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties) {
        if (index.get() < PROCESSORS.size()) {
            AnnoBeanUtils.getBean(PROCESSORS.get(index.getAndIncrement())).doProcessor(amisBaseWrapper, clazz, properties, this);
        }
    }
}
