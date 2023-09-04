package site.sorghum.anno.solon;

import org.noear.solon.core.AppContext;
import site.sorghum.anno._common.AnnoBean;

import java.util.List;

/**
 * @author songyinyin
 * @since 2023/7/30 15:56
 */
public class SolonBeanImpl implements AnnoBean {

    private final AppContext context;

    public SolonBeanImpl(AppContext context) {
        this.context = context;
    }

    @Override
    public <T> T getBean(String name) {
        return context.getBean(name);
    }

    @Override
    public <T> T getBean(Class<T> type) {
        return context.getBean(type);
    }

    @Override
    public <T> List<T> getBeansOfType(Class<T> baseType) {
        return context.getBeansOfType(baseType);
    }
}
