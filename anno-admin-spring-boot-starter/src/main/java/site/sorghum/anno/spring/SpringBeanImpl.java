package site.sorghum.anno.spring;

import cn.hutool.extra.spring.SpringUtil;
import site.sorghum.anno._common.AnnoBean;

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
        return SpringUtil.getBean(type);
    }

    @Override
    public <T> List<T> getBeansOfType(Class<T> baseType) {
        return SpringUtil.getBeansOfType(baseType).values().stream().toList();
    }
}
