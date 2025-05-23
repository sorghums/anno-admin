package site.sorghum.anno.db.dao;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.util.GenericsUtil;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.db.exception.AnnoDbException;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno.db.service.context.AnnoDbContext;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Anno基准dao
 *
 * @author Sorghum
 * @since 2024/01/29
 */
public interface AnnoBaseDao<T> {

    AtomicReference<DbService> DB_SERVICE = new AtomicReference<>();

    AtomicReference<MetadataManager> METADATA_MANAGER = new AtomicReference<>();

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
     * 按条件查找一个
     *
     * @param criteria 查询条件
     * @return {@link T}
     */
    default T findOne(DbCriteria criteria) {
        return dbService().queryOne(
            criteria
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
    default List<T> findByIds(List<? extends Serializable> ids) {
        if (CollUtil.isEmpty(ids)) {
            return new LinkedList<>();
        }
        return list(DbCriteria.fromClass(entityClass()).in("id", ids.toArray()));
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
     * 删去
     *
     * @param dbCriteria db标准
     * @return int
     */
    default int delete(DbCriteria dbCriteria) {
        return dbService().delete(dbCriteria);
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
        String pkJavaFieldName = getPkJavaFieldName();
        Object id = ReflectUtil.getFieldValue(t, pkJavaFieldName);
        if (id == null || StrUtil.isBlankIfStr(id)) {
            ReflectUtil.setFieldValue(t, pkJavaFieldName, IdUtil.getSnowflakeNextIdStr());
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
     * 计数
     *
     * @param dbCriteria db标准
     * @return long
     */
    default long count(DbCriteria dbCriteria) {
        return AnnoDbContext.dynamicDbContext(
            anEntity().dbName(),
            () -> {
                return dbService().count(dbCriteria);
            }
        );
    }

    /**
     * sql查询一个
     *
     * @param sql    sql
     * @param params params
     * @return {@link T}
     */
    default T sqlOne(String sql, Object... params) {
        return AnnoDbContext.dynamicDbContext(
            anEntity().dbName(),
            () -> {
                return dbService().sqlQueryOne(
                    entityClass(), sql, params
                );
            }
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
        return AnnoDbContext.dynamicDbContext(
            anEntity().dbName(),
            () -> {
                return dbService().sqlQueryList(
                    entityClass(), sql, params
                );
            }
        );
    }

    /**
     * db标准
     * 查询包装器
     *
     * @return {@link DbCriteria}
     */
    default DbCriteria dbCriteria(){
        return DbCriteria.fromClass(entityClass());
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
        return metadataManager().getEntity(entityClass()).getPkField().getJavaName();
    }



    /**
     * 数据库服务
     *
     * @return {@link DbService}
     */
    default DbService dbService(){
        if (DB_SERVICE.get() == null){
            DB_SERVICE.set(AnnoBeanUtils.getBean(DbService.class));
        }
        return DB_SERVICE.get();
    };

    /**
     * 元数据管理器
     *
     * @return {@link MetadataManager}
     */
    default MetadataManager metadataManager(){
        if (METADATA_MANAGER.get() == null){
            METADATA_MANAGER.set(AnnoBeanUtils.getBean(MetadataManager.class));
        }
        return METADATA_MANAGER.get();
    }

    default AnEntity anEntity(){
        return metadataManager().getEntity(this.entityClass());
    }

    /**
     * 实体类
     *
     * @return {@link Class}<{@link T}>
     */
    default Class<T> entityClass(){
        Class<? extends AnnoBaseDao> nowClass = this.getClass();
        return (Class<T>) GenericsUtil.getInterfaceGenericsType(nowClass, AnnoBaseDao.class, 0);
    }
}
