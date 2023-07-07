package site.sorghum.anno.modular.amis.process.processer;

import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;
import site.sorghum.anno.modular.amis.process.processer.crud.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * crud处理器链
 *
 * @author Sorghum
 * @since 2023/07/07
 */
public class CrudProcessorChain implements BaseProcessorChain {
    /**
     * 处理器索引
     */
    private final AtomicInteger index = new AtomicInteger(0);
    private static final List<BaseProcessor> PROCESSORS = List.of(
            // 初始化
            new CrudViewInitProcessor(),
            // 增删改查的列信息
            new CrudColumnProcessor(),
            // 过滤的表单信息
            new CrudFilterProcessor(),
            // 每一行的删除信息
            new CrudDeleteBtnProcessor(),
            // 每一行的编辑信息
            new CrudEditInfoProcessor(),
            // 每一行的自定义按钮信息
            new CrudColumnButtonProcessor(),
            // 每一行的新增信息
            new CrudAddInfoProcessor(),
            // 边栏的树信息
            new CrudTreeAsideProcessor(),
            // 多对多的多选框信息
            new CrudM2mCheckProcessor()

    );
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties){
        if (index.get() < PROCESSORS.size()) {
            PROCESSORS.get(index.getAndIncrement()).doProcessor(amisBaseWrapper, clazz, properties, this);
        }
    }
}
