package site.sorghum.anno.amis.process.processer.crudm2m;

import jakarta.inject.Named;
import site.sorghum.amis.entity.AmisBaseWrapper;
import site.sorghum.amis.entity.display.Crud;
import site.sorghum.amis.entity.display.DrawerButton;
import site.sorghum.anno.amis.model.CrudM2mView;
import site.sorghum.anno.amis.process.BaseProcessor;
import site.sorghum.anno.amis.process.BaseProcessorChain;
import site.sorghum.anno.amis.process.processer.CrudProcessorChain;

import java.util.List;
import java.util.Map;

/**
 * crud-m2m关系数据处理机
 *
 * @author Sorghum
 * @since 2023/07/10
 */
@Named
public class CrudM2mRelationDataProcessor implements BaseProcessor {
    @Override
    public void doProcessor(AmisBaseWrapper amisBaseWrapper, Class<?> clazz, Map<String, Object> properties, BaseProcessorChain chain) {
        AmisBaseWrapper crudWrap = AmisBaseWrapper.of();
        CrudProcessorChain crudProcessorChain = new CrudProcessorChain();
        crudProcessorChain.doProcessor(crudWrap, clazz, properties);

        CrudM2mView crudM2mView = (CrudM2mView) amisBaseWrapper.getAmisBase();
        Crud crudBody = crudM2mView.getCrudBody();
        List<Object> headerToolbar = crudBody.getHeaderToolbar();
        Object obj = headerToolbar.get(2);
        DrawerButton drawerButton = (DrawerButton) obj;
        DrawerButton.Drawer drawer = drawerButton.getDrawer();
        drawer.setBody(crudWrap.getAmisBase());
        chain.doProcessor(amisBaseWrapper, clazz, properties);
    }
}
