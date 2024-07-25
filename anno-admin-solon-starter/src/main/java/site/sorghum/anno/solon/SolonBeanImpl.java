package site.sorghum.anno.solon;

import cn.hutool.core.util.StrUtil;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.BeanWrap;
import site.sorghum.anno._common.AnnoBean;
import site.sorghum.anno._common.exception.BizException;

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
        List<T> beansOfType = context.getBeansOfType(type);
        if (beansOfType == null || beansOfType.isEmpty()) {
            throw new BizException(
                "未找到" + type.getSimpleName() + "的代理bean，" +
                "请检查是否在solon容器中注册了该bean。");
        }
        return beansOfType.get(0);
    }

    @Override
    public <T> List<T> getBeansOfType(Class<T> baseType) {
        return context.getBeansOfType(baseType);
    }

    @Override
    public String getBeanName(Class aClass) {
        List<BeanWrap> wrapsOfType = context.getWrapsOfType(aClass);
        if (wrapsOfType == null || wrapsOfType.isEmpty()) {
            throw new BizException(
                "未找到" + aClass.getSimpleName() + "的代理bean，" +
                "请检查是否在solon容器中注册了该bean。");
        }
        String name = wrapsOfType.get(0).name();
        if (StrUtil.isBlank(name)) {
            name = StrUtil.lowerFirst(aClass.getSimpleName());
        }
        return name;
    }

    @Override
    public void registerBean(String name, Object bean) {
        context.beanInject(bean);
        context.beanRegister(new BeanWrap(context, bean.getClass(), bean, name), name, true);
    }

    @Override
    public void unregisterBean(String name) {
        context.removeWrap(name);
    }
}
