package site.sorghum.anno.spring;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import site.sorghum.anno._common.AnnoBean;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;

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

    @Override
    public String getBeanName(Class aClass) {
        String[] beanNamesForType = SpringUtil.getBeanNamesForType(aClass);
        if (beanNamesForType.length > 0) {
            String name = beanNamesForType[0];
            if (StrUtil.isBlank(name)){
                name = StrUtil.lowerFirst(aClass.getSimpleName());
            }
            return name;
        }
        throw new BizException(
                "未找到" + aClass.getSimpleName() + "的代理bean，" +
                "请检查是否在spring容器中注册了该bean。"
        );
    }
}
