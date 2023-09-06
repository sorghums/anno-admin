package site.sorghum.anno.db.service;


import org.noear.wood.IPage;
import site.sorghum.anno.db.param.DbCondition;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.TableParam;

import java.util.List;

/**
 * Sql构造器
 *
 * @author sorghum
 * @since 2023/07/07
 */
public interface DbService {
    /**
     * 分页查询
     *
     * @param tClass   类
     * @param dbConditions db条件
     * @param pageParam    页面参数
     * @return {@link IPage}<{@link T}>
     */
    <T> IPage<T> page(Class<T> tClass, List<DbCondition> dbConditions, PageParam pageParam);


    /**
     * 列表查询
     *
     * @param tClass   类
     * @param dbConditions db条件
     * @return {@link List}<{@link T}>
     */
    <T> List<T> list(Class<T> tClass, List<DbCondition> dbConditions);

    /**
     * 查询单个数据
     *
     * @param tClass   类
     * @param dbConditions db条件
     * @return {@link T}
     */
    <T> T queryOne(Class<T> tClass, List<DbCondition> dbConditions);

    /**
     * 更新
     *
     * @param dbConditions db条件
     * @param t            数据
     * @return int
     */
    <T> int update(List<DbCondition> dbConditions, T t);

    /**
     * 插入
     *
     * @param t          数据
     * @return int
     */
    <T> long insert(T t);

    /**
     * 删除
     *
     * @param tClass   类
     * @param dbConditions db条件
     * @return int
     */
    <T> int delete(Class<T> tClass, List<DbCondition> dbConditions);
}
