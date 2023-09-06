package site.sorghum.anno.anno.proxy;

import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;

import java.util.List;

/**
 * Anno代理
 *
 * @author sorghum
 * @since 2023/05/20
 */
public interface AnnoPreBaseProxy<T> {
    /**
     * 之前添加
     * 增加前
     *
     * @param data 数据
     */
    default void beforeAdd(T data){};

    /**
     * 增加后
     *
     * @param data 数据
     */
    default void afterAdd(T data){};

    /**
     * 在更新之前
     *
     * @param dbConditions db条件
     * @param data         data
     */
    default void beforeUpdate(List<DbCondition> dbConditions, T data){};


    /**
     * 修改后
     *
     * @param data 数据
     */
    default void afterUpdate(T data){};

    /**
     * 在删除之前
     *
     * @param tClass       表参数
     * @param dbConditions db条件
     */
    default void beforeDelete(Class<T> tClass, List<DbCondition> dbConditions){};

    /**
     * 删除后
     *
     * @param tClass       类
     * @param dbConditions db条件
     */
    default void afterDelete(Class<T> tClass, List<DbCondition> dbConditions){};

    /**
     * 查询前，返回值为：自定义查询条件
     *
     * @param tClass       表参数
     * @param dbConditions db条件
     * @param pageParam    页面参数
     */
    default void beforeFetch(Class<T> tClass, List<DbCondition> dbConditions, PageParam pageParam){};

    /**
     * 返回结果后
     *
     * @param tClass       表参数
     * @param dbConditions db条件
     * @param pageParam    页面参数
     * @param page         列表
     */
    default void afterFetch(Class<T> tClass, List<DbCondition> dbConditions, PageParam pageParam, AnnoPage<T> page){};

}
