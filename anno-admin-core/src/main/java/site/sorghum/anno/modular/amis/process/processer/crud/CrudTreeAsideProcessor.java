package site.sorghum.anno.modular.amis.process.processer.crud;

import cn.hutool.core.collection.CollUtil;
import org.noear.solon.annotation.Component;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.function.Api;
import site.sorghum.amis.entity.input.InputTree;
import site.sorghum.anno.modular.amis.model.CrudView;
import site.sorghum.anno.modular.amis.process.BaseProcessor;
import site.sorghum.anno.modular.amis.process.BaseProcessorChain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoLeftTree;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.modular.anno.util.AnnoUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * CRUD视图初始化处理器
 *
 * @author Sorghum
 * @since 2023/07/07
 */
@Component
public class CrudTreeAsideProcessor implements BaseProcessor {
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain){
        CrudView crudView = (CrudView) amisBaseWrapper.getAmisBase();
        AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
        AnnoLeftTree annoLeftTree = annoMain.annoLeftTree();
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
            setUrl("/system/anno/${treeClazz}/annoTrees");
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
                            put(annoLeftTree.catKey(), "${_cat}");
                        }});
                    }}
            ));
        }});
        tree.setOnEvent(event);
        AnnoTree annoTree = annoMain.annoTree();
        boolean enableTreeAside = annoLeftTree.enable() || (annoTree.enable() && annoTree.displayAsTree());
        if (enableTreeAside) {
            crudView.setAside(tree);
            return;
        }
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
