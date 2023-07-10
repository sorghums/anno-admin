package site.sorghum.anno.modular.amis.process.processer;

import org.noear.solon.Solon;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;
import site.sorghum.anno.modular.amis.process.processer.crud.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 增删改查处理器链
 *
 * @author Sorghum
 * @since 2023/07/07
 */
public class CrudProcessorChain implements BaseProcessorChain {
    /**
     * 处理器索引
     */
    private final AtomicInteger index = new AtomicInteger(0);
    private static final List<Class<? extends BaseProcessor>> PROCESSORS = List.of(
            // 初始化
            CrudViewInitProcessor.class,
            // 增删改查的列信息
            CrudColumnProcessor.class,
            // 过滤的表单信息
            CrudFilterProcessor.class,
            // 行的删除信息
            CrudDeleteBtnProcessor.class,
            // 行的编辑信息
            CrudEditInfoProcessor.class,
            // 行的自定义按钮信息
            CrudColumnButtonProcessor.class,
            // 行的新增信息
            CrudAddInfoProcessor.class,
            // 边栏的树信息
            CrudTreeAsideProcessor.class,
            // 多对多的多选框信息
            CrudM2mCheckProcessor.class

    );
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties){
        if (index.get() < PROCESSORS.size()) {
            Solon.context().getBean(PROCESSORS.get(index.getAndIncrement())).doProcessor(amisBaseWrapper, clazz, properties, this);
        }
    }
}
