package site.sorghum.anno.modular.amis.process.processer.tree;

import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.anno.modular.amis.model.TreeView;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;

import java.util.Map;

/**
 * 树视图初始化处理器
 *
 * @author Sorghum
 * @since 2023/07/10
 */
@Named
public class TreeViewInitProcessor implements BaseProcessor {
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        TreeView treeView = TreeView.of();
        amisBaseWrapper.setAmisBase(treeView);
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
