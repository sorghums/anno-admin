package site.sorghum.anno.modular.anno.proxy;

import cn.hutool.core.lang.Tuple;
import org.noear.wood.DbTableQuery;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Anno代理
 *
 * @author sorghum
 * @since 2023/05/20
 */
public class AnnoBaseProxy<T> {
    /**
     * 增加前
     *
     * @param data 数据
     */
    public void beforeAdd(T data) {
    }

    /**
     * 增加后
     *
     * @param data 数据
     */
    public void afterAdd(T data) {
    }

    /**
     * 修改前
     *
     * @param data 数据
     */
    public void beforeUpdate(T data) {
    }


    /**
     * 修改后
     *
     * @param data 数据
     */
    public void afterUpdate(T data) {
    }

    /**
     * 删除前
     *
     * @param id id
     */
    public void beforeDelete(Serializable id) {
    }

    /**
     * 删除前
     *
     * @param tuples tuples
     */
    public void beforeDelete(List<Tuple> tuples) {
    }

    /**
     * 删除后
     *
     * @param id id
     */
    public void afterDelete(Serializable id) {
    }

    /**
     * 删除后
     *
     * @param id id
     */
    public void afterDelete(List<Tuple> tuples) {
    }

    /**
     * 查询前，返回值为：自定义查询条件
     *
     * @param dbTableQuery 数据库表查询
     */
    public void beforeFetch(DbTableQuery dbTableQuery) {
    }

    /**
     * 返回结果后
     *
     * @param dataList 列表
     */
    public void afterFetch(Collection<T> dataList) {
    }


}
