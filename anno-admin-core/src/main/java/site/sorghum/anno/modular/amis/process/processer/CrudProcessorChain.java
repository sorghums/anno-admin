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
    private final AtomicInteger index = new AtomicInteger(0);
    List<BaseProcessor> processors = List.of(
            new CrudViewInitProcessor(),
            new CrudColumnProcessor(),
            new CrudFilterProcessor(),
            new CrudDeleteBtnProcessor(),
            new CrudEditInfoProcessor(),
            new CrudColumnButtonProcessor(),
            new CrudAddInfoProcessor(),
            new CrudTreeAsideProcessor(),
            new CrudM2mCheckProcessor()

    );
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties){
        if (index.get() < processors.size()) {
            processors.get(index.getAndIncrement()).doProcessor(amisBaseWrapper, clazz, properties, this);
        }
    }
}
