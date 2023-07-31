package site.sorghum.anno.amis.process.processer.crudm2m;

import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.anno.amis.model.CrudM2mView;
import site.sorghum.anno.amis.process.BaseProcessor;
import site.sorghum.anno.amis.process.BaseProcessorChain;

import java.util.Map;

/**
 * crud-m2m视图初始化处理器
 *
 * @author Sorghum
 * @since 2023/07/10
 */
@Named
public class CrudM2mViewInitProcessor implements BaseProcessor {
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        CrudM2mView crudM2mView = CrudM2mView.of();
        amisBaseWrapper.setAmisBase(crudM2mView);
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
