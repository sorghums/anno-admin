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
     * @param tableParam   表参数
     * @param dbConditions db条件
     * @param pageParam    页面参数
     * @return {@link IPage}<{@link T}>
     */
    <T> IPage<T> page(TableParam<T> tableParam, List<DbCondition> dbConditions, PageParam pageParam);


    /**
     * 列表查询
     *
     * @param tableParam   表参数
     * @param dbConditions db条件
     * @return {@link List}<{@link T}>
     */
    <T> List<T> list(TableParam<T> tableParam, List<DbCondition> dbConditions);

    /**
     * 查询单个数据
     *
     * @param tableParam   表参数
     * @param dbConditions db条件
     * @return {@link T}
     */
    <T> T queryOne(TableParam<T> tableParam, List<DbCondition> dbConditions);

    /**
     * 更新
     *
     * @param tableParam   表参数
     * @param dbConditions db条件
     * @param t            数据
     * @return int
     */
    <T> int update(TableParam<T> tableParam, List<DbCondition> dbConditions, T t);

    /**
     * 插入
     *
     * @param tableParam 表参数
     * @param t          数据
     * @return int
     */
    <T> long insert(TableParam<T> tableParam, T t);

    /**
     * 删除
     *
     * @param tableParam   表参数
     * @param dbConditions db条件
     * @return int
     */
    <T> int delete(TableParam<T> tableParam, List<DbCondition> dbConditions);
}
