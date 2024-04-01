package site.sorghum.anno._common;

import java.util.List;

/**
 * 接口用于访问一组相关的Bean对象。
 *
 * @author songyinyin
 * @since 2023/7/30 15:46
 */
public interface AnnoBean {

    /**
     * 根据名称获取单个Bean对象。
     *
     * @param name Bean的名称。
     * @param <T>  Bean的类型。
     * @return 返回找到的Bean对象。
     */
    <T> T getBean(String name);

    /**
     * 根据类型获取单个Bean对象。
     *
     * @param type Bean的类型。
     * @param <T>  Bean的类型。
     * @return 返回找到的Bean对象。
     */
    <T> T getBean(Class<T> type);

    /**
     * 根据类型获取所有相应类型的Bean对象集合。
     *
     * @param baseType Bean的基类类型。
     * @param <T>  Bean的类型。
     * @return 返回包含所有相应类型的Bean对象集合。
     */
    <T> List<T> getBeansOfType(Class<T> baseType);

    /**
     * 获取bean名称
     *
     * @param aClass 一个班
     * @return {@link String}
     */
    String getBeanName(Class aClass);
}
