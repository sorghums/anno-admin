package site.sorghum.anno.anno.proxy;

import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;

import java.util.List;

/**
 * Anno代理
 *
 * @author sorghum
 * @since 2023/05/20
 */
public interface AnnoBaseProxy<T> {
    /**
     * 增加前
     *
     * @param data       数据
     * @param tableParam 表参数
     */
    void beforeAdd(TableParam<T> tableParam, T data);

    /**
     * 增加后
     *
     * @param data 数据
     */
    void afterAdd(T data);

    /**
     * 在更新之前
     *
     * @param tableParam   表参数
     * @param dbConditions db条件
     * @param data            data
     */
    void beforeUpdate(TableParam<T> tableParam, List<DbCondition> dbConditions, T data);


    /**
     * 修改后
     *
     * @param data 数据
     */
    void afterUpdate(T data);

    /**
     * 在删除之前
     *
     * @param tableParam   表参数
     * @param dbConditions db条件
     */
    void beforeDelete(TableParam<T> tableParam, List<DbCondition> dbConditions);

    /**
     * 删除后
     *
     * @param dbConditions db条件
     */
    void afterDelete(List<DbCondition> dbConditions);

    /**
     * 查询前，返回值为：自定义查询条件
     *
     * @param tableParam   表参数
     * @param dbConditions db条件
     * @param pageParam    页面参数
     */
    void beforeFetch(TableParam<T> tableParam, List<DbCondition> dbConditions, PageParam pageParam);

    /**
     * 返回结果后
     *
     * @param tableParam   表参数
     * @param dbConditions db条件
     * @param pageParam    页面参数
     * @param page         分页结果数据
     */
    void afterFetch(TableParam<T> tableParam, List<DbCondition> dbConditions, PageParam pageParam, AnnoPage<T> page);


}
