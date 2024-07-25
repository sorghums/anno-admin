package site.sorghum.anno.spring;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import site.sorghum.anno._common.AnnoBean;
import site.sorghum.anno._common.exception.BizException;

import java.util.List;

/**
 * @author songyinyin
 * @since 2024/1/18 11:47
 */
public class SpringBeanImpl implements AnnoBean {
    @Override
    public <T> T getBean(String name) {
        return SpringUtil.getBean(name);
    }

    @Override
    public <T> T getBean(Class<T> type) {
        List<T> beansOfType = getBeansOfType(type);
        if (beansOfType.size() > 1) {
            throw new BizException(
                "未找到唯一" + type.getSimpleName() + "的代理bean，" +
                "请检查是否在spring容器中注册了该bean。"
            );
        } else if (beansOfType.isEmpty()) {
            return _getBean(type);
        }
        return beansOfType.get(0);
    }

    /**
     * 获取指定类型的Bean对象
     *
     * @param type 要获取的Bean对象的类型
     * @param <T>  Bean对象的类型
     * @return 返回指定类型的Bean对象
     */
    private <T> T _getBean(Class<T> type) {
        return SpringUtil.getBean(type);
    }

    @Override
    public <T> List<T> getBeansOfType(Class<T> baseType) {
        return SpringUtil.getBeansOfType(baseType).values().stream().toList();
    }

    @Override
    public String getBeanName(Class aClass) {
        String[] beanNamesForType = SpringUtil.getBeanNamesForType(aClass);
        if (beanNamesForType.length > 0) {
            String name = beanNamesForType[0];
            if (StrUtil.isBlank(name)) {
                name = StrUtil.lowerFirst(aClass.getSimpleName());
            }
            return name;
        }
        throw new BizException(
            "未找到" + aClass.getSimpleName() + "的代理bean，" +
            "请检查是否在spring容器中注册了该bean。"
        );
    }

    @Override
    public void unregisterBean(String name) {
        SpringUtil.unregisterBean(name);
    }

    @Override
    public void registerBean(String name, Object bean) {
        SpringUtil.registerBean(name, bean);
    }
}
