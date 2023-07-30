package site.sorghum.anno.modular.amis.process.processer.treem2m;

import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.anno.modular.amis.model.TreeM2mView;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;

import java.util.Map;

/**
 * 树-m2m视图初始化处理器
 *
 * @author Sorghum
 * @since 2023/07/10
 */
@Named
public class TreeM2mViewInitProcessor implements BaseProcessor {
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        TreeM2mView treeM2mView = TreeM2mView.of();
        amisBaseWrapper.setAmisBase(treeM2mView);
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
