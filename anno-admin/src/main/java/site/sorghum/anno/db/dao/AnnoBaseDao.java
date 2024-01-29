package site.sorghum.anno.db.dao;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.noear.wood.utils.StringUtils;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.exception.AnnoDbException;
import site.sorghum.anno.db.service.DbService;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Anno基准dao
 *
 * @author Sorghum
 * @since 2024/01/29
 */
public interface AnnoBaseDao<T> {
    /**
     * 数据库服务
     *
     * @return {@link DbService}
     */
    DbService dbService();

    /**
     * 元数据管理器
     *
     * @return {@link MetadataManager}
     */
    MetadataManager metadataManager();

    /**
     * 实体类
     *
     * @return {@link Class}<{@link T}>
     */
    Class<T> entityClass();

    /**
     * 按id查找
     *
     * @param id 身份证件
     * @return {@link T}
     */
    default T findById(Serializable id) {
        return dbService().queryOne(
            DbCriteria.fromClass(entityClass()).eq(getPkDbFieldName(), id)
        );
    }

    /**
     * 按id查找选项
     *
     * @param id id
     * @return {@link Optional}<{@link T}>
     */
    default Optional<T> findByIdOpt(Serializable id) {
        return Optional.ofNullable(dbService().queryOne(
            DbCriteria.fromClass(entityClass()).eq(getPkDbFieldName(), id)
        ));
    }

    /**
     * 按ID查找
     *
     * @param ids ids
     * @return {@link List}<{@link T}>
     */
    default List<T> findByIds(List<? extends Serializable> ids){
        if (CollUtil.isEmpty(ids)) {
            return new LinkedList<>();
        }
        return list(DbCriteria.fromClass(entityClass()).in("id", ids));
    }


    /**
     * 按id删除
     *
     * @param id 身份证件
     * @return int
     */
    default int deleteById(Serializable id) {
        return dbService().delete(
            DbCriteria.fromClass(entityClass()).eq(getPkDbFieldName(), id)
        );
    }

    /**
     * 插入
     *
     * @param t t
     * @return long
     */
    default long insert(T t) {
        return dbService().insert(t);
    }

    /**
     * 插入列表
     *
     * @param list 列表
     * @return long
     */
    default long insertList(List<T> list) {
        long num = 0;
        for (T t : list) {
            num = num + insert(t);
        }
        return num;
    }

    /**
     * 按id列表更新
     *
     * @param list 列表
     * @return long
     */
    default long updateByIdList(List<T> list) {
        long num = 0;
        for (T t : list) {
            num = num + updateById(t);
        }
        return num;
    }

    /**
     * 按id更新
     *
     * @param t t
     * @return int
     */
    default int updateById(T t) {
        Object id = ReflectUtil.getFieldValue(t, getPkJavaFieldName());
        if (StrUtil.isEmptyIfStr(id)) {
            throw new AnnoDbException("UnKnow %s's id field or id field's value is empty.".formatted(entityClass()));
        }
        return dbService().update(t, DbCriteria.fromClass(entityClass()).eq(getPkDbFieldName(), id));
    }


    /**
     * 使现代化
     *
     * @param t          t
     * @param dbCriteria db标准
     * @return int
     */
    default int update(T t, DbCriteria dbCriteria) {
        return dbService().update(t, dbCriteria);
    }

    /**
     * 列表
     *
     * @return {@link List}<{@link T}>
     */
    default List<T> list() {
        return dbService().list(DbCriteria.fromClass(entityClass()));
    }

    /**
     * 列表
     *
     * @param dbCriteria db标准
     * @return {@link List}<{@link T}>
     */
    default List<T> list(DbCriteria dbCriteria) {
        return dbService().list(dbCriteria);
    }

    /**
     * 分页
     *
     * @param currentPage 当前页面
     * @param pageSize    页大小
     * @return {@link AnnoPage}<{@link T}>
     */
    default AnnoPage<T> page(int currentPage, int pageSize) {
        DbCriteria dbCriteria = DbCriteria.fromClass(entityClass());
        dbCriteria.page(currentPage, pageSize);
        return dbService().page(dbCriteria);
    }

    /**
     * 分页
     *
     * @param dbCriteria db标准
     * @return {@link AnnoPage}<{@link T}>
     */
    default AnnoPage<T> page(DbCriteria dbCriteria) {
        return dbService().page(dbCriteria);
    }


    /**
     * 插入或更新
     *
     * @param t t
     * @return int
     */
    default long insertOrUpdate(T t) {
        Object id = ReflectUtil.getFieldValue(t, getPkJavaFieldName());
        if (id == null || StrUtil.isBlankIfStr(id)) {
            return insert(t);
        } else {
            return updateById(t);
        }
    }

    /**
     * 保存或更新
     *
     * @param t t
     * @return int
     */
    default long saveOrUpdate(T t) {
        return insertOrUpdate(t);
    }

    /**
     * sql查询一个
     *
     * @param sql    sql
     * @param params params
     * @return {@link T}
     */
    default T sqlOne(String sql, Object... params) {
        return dbService().sqlQueryOne(
            entityClass(), sql, params
        );
    }

    /**
     * sql查询列表
     *
     * @param sql    sql
     * @param params params
     * @return {@link List}<{@link T}>
     */
    default List<T> sqlList(String sql, Object... params) {
        return dbService().sqlQueryList(
            entityClass(), sql, params
        );
    }

    /**
     * 获取pk-db字段名
     *
     * @return {@link String}
     */
    default String getPkDbFieldName() {
        return metadataManager().getEntity(entityClass()).getPkField().getTableFieldName();
    }

    /**
     * 获取pk-java字段名
     *
     * @return {@link String}
     */
    default String getPkJavaFieldName() {
        return metadataManager().getEntity(entityClass()).getPkField().getFieldName();
    }

}
