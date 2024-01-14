package site.sorghum.anno.db.service;


import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.db.DbCriteria;

import java.util.List;
import java.util.Map;

/**
 * Sql构造器
 *
 * @author sorghum
 * @since 2023/07/07
 */
public interface DbService {
    /**
     * 分页查询
     */
    <T> AnnoPage<T> page(DbCriteria criteria);


    /**
     * 列表查询
     *
     * @param criteria db条件
     * @return {@link List}<{@link T}>
     */
    <T> List<T> list(DbCriteria criteria);

    /**
     * 查询单个数据
     *
     * @param criteria db条件
     * @return {@link T}
     */
    <T> T queryOne(DbCriteria criteria);

    /**
     * 更新
     *
     * @param t        数据
     * @param criteria db条件
     * @return int
     */
    <T> int update(T t, DbCriteria criteria);

    /**
     * 插入
     *
     * @param t 数据
     * @return int
     */
    <T> long insert(T t);

    /**
     * 删除
     *
     * @param criteria db条件
     * @return int
     */
    <T> int delete(DbCriteria criteria);

    /**
     * sql直接查询
     *
     * @param actualSql sql
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> sql2MapList(String actualSql);
}
