package site.sorghum.anno.modular.amis.process.processer;

import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.anno.common.AnnoBeanUtils;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;
import site.sorghum.anno.modular.amis.process.processer.crudm2m.CrudM2mColumnProcessor;
import site.sorghum.anno.modular.amis.process.processer.crudm2m.CrudM2mEditInfoProcessor;
import site.sorghum.anno.modular.amis.process.processer.crudm2m.CrudM2mFilterProcessor;
import site.sorghum.anno.modular.amis.process.processer.crudm2m.CrudM2mRelationDataProcessor;
import site.sorghum.anno.modular.amis.process.processer.crudm2m.CrudM2mRemoveRelationProcessor;
import site.sorghum.anno.modular.amis.process.processer.crudm2m.CrudM2mViewInitProcessor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 增删改查多对多处理器链
 *
 * @author Sorghum
 * @since 2023/07/10
 */
public class CrudM2mProcessorChain implements BaseProcessorChain {
    /**
     * 处理器索引
     */
    private final AtomicInteger index = new AtomicInteger(0);
    private static final List<Class<? extends BaseProcessor>> PROCESSORS = List.of(
        // 初始化
        CrudM2mViewInitProcessor.class,
        // 查询信息
        CrudM2mFilterProcessor.class,
        // 增删改查的列信息
        CrudM2mColumnProcessor.class,
        // 添加关联查询的表格信息
        CrudM2mRelationDataProcessor.class,
        // 添加编辑信息
        CrudM2mEditInfoProcessor.class,
        // 添加删除对应关联关系信息的按钮
        CrudM2mRemoveRelationProcessor.class

    );

    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties) {
        if (index.get() < PROCESSORS.size()) {
            AnnoBeanUtils.getBean(PROCESSORS.get(index.getAndIncrement())).doProcessor(amisBaseWrapper, clazz, properties, this);
        }
    }
}
