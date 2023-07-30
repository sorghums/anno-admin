package site.sorghum.anno.common;

import java.util.List;

/**
 * bean 容器接口抽象
 *
 * @author songyinyin
 * @since 2023/7/30 15:46
 */
public interface AnnoBean {

    /**
     * 通过名称 获取 bean
     */
    <T> T getBean(String name);

    /**
     * 通过类型 获取 bean
     */
    <T> T getBean(Class<T> type);

    /**
     * 通过类型 获取 bean 集合
     */
    <T> List<T> getBeansOfType(Class<T> baseType);

}
