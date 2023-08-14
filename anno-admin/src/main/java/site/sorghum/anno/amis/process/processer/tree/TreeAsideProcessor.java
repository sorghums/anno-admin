package site.sorghum.anno.amis.process.processer.tree;

import cn.hutool.core.collection.CollUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.InputTree;
import site.sorghum.anno.amis.model.TreeView;
import site.sorghum.anno.amis.process.BaseProcessor;
import site.sorghum.anno.amis.process.BaseProcessorChain;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 树边栏处理器
 *
 * @author Sorghum
 * @since 2023/07/10
 */
@Named
public class TreeAsideProcessor implements BaseProcessor {

    @Inject
    MetadataManager metadataManager;

    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        TreeView treeView = (TreeView) amisBaseWrapper.getAmisBase();
        AnEntity anEntity = metadataManager.getEntity(clazz);
        InputTree tree = new InputTree();
        tree.setId("aside-input-tree");
        tree.setName("_cat");
        tree.setSearchable(true);
        tree.setMultiple(false);
        tree.setCascade(false);
        tree.setHeightAuto(false);
        tree.setVirtualThreshold(9999);
        tree.setInputClassName("no-border no-padder mt-1");
        tree.setUnfoldedLevel(2);
        tree.setShowOutline(true);
        tree.setSource(new Api() {{
            setMethod("get");
            setUrl("/amis/system/anno/${treeClazz}/annoTrees");
        }});
        Map<String, Object> event = new HashMap<>();
        event.put("change", new HashMap<String, Object>() {{
            put("actions", CollUtil.newArrayList(
                new HashMap<String, Object>() {{
                    put("actionType", "broadcast");
                    put("args", new HashMap<String, Object>() {{
                        put("eventName", "broadcast_aside_change");
                    }});
                    put("data", new HashMap<String, Object>() {{
                        put("_cat", "${_cat}");
                    }});
                }}
            ));
        }});
        tree.setOnEvent(event);
        boolean enableTreeAside = anEntity.isEnableLeftTree() || (anEntity.isEnableTree() && anEntity.isTreeDisplayAsTree());
        if (enableTreeAside) {
            treeView.setAside(tree);
        }
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
