package site.sorghum.anno.modular.amis.process.processer.crud;

import org.noear.solon.annotation.Component;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.anno.modular.amis.model.CrudView;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;

import java.util.Map;

/**
 * CRUD视图初始化处理器
 *
 * @author Sorghum
 * @since 2023/07/07
 */
@Component
public class CrudViewInitProcessor implements BaseProcessor {
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain){
        CrudView page = CrudView.of();
        amisBaseWrapper.setAmisBase(page);
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
