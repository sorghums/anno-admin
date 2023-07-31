package site.sorghum.anno.amis.process.processer.crud;

import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.anno.amis.model.CrudView;
import site.sorghum.anno.amis.process.BaseProcessor;
import site.sorghum.anno.amis.process.BaseProcessorChain;

import java.util.Map;

/**
 * CRUD视图初始化处理器
 *
 * @author Sorghum
 * @since 2023/07/07
 */
@Named
public class CrudViewInitProcessor implements BaseProcessor {
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        CrudView page = CrudView.of();
        amisBaseWrapper.setAmisBase(page);
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
