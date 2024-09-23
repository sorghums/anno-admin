package site.sorghum.anno.anno.proxy;

import cn.hutool.core.util.StrUtil;
import org.slf4j.LoggerFactory;
import site.sorghum.anno._common.util.GenericsUtil;
import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.anno.util.AnnoFieldCache;
import site.sorghum.anno.db.DbCondition;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.method.MethodTemplate;
import site.sorghum.anno.method.route.EntityMethodRoute;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Anno代理
 *
 * @author sorghum
 * @since 2023/05/20
 */
@MethodTemplate(route = EntityMethodRoute.class)
public interface AnnoBaseProxy<T> {

    /**
     * 在哪些 entity 上生效，Object 代表所有，为空则不生效
     *
     * @return entityName
     */
    default String[] supportEntities() {
        return new String[]{
            clazzToDamiEntityName(entityClass())
        };
    }

    /**
     * 从给定的数据库条件中获取主键值。
     *
     * @param dbCondition 数据库条件对象，包含字段名和对应的值
     * @param entityClass 实体类对象，用于获取主键字段名
     * @return 返回主键值，如果未找到则返回null
     */
    default String getPkValueFromDbCriteria(DbCondition dbCondition, Class<?> entityClass) {
        String pkName = AnnoFieldCache.getPkName(entityClass);
        return getValueStringFromDbCriteria(dbCondition, pkName);
    }

    /**
     * 从数据库条件中获取指定Java属性名的值，并转换为字符串返回
     *
     * @param dbCondition 数据库条件对象
     * @param javaName Java属性名
     * @return 转换后的字符串值，若未找到则返回null
     */
    default String getValueStringFromDbCriteria(DbCondition dbCondition, String javaName) {
        if (Objects.equals(dbCondition.getField(), javaName)) {
            for (Object value : dbCondition.getValues()) {
                return StrUtil.toString(value);
            }
        } else {
            for (Object value : dbCondition.getValues()) {
                if (value instanceof DbCondition _dbCondition) {
                    String dbCriteria = getValueStringFromDbCriteria(_dbCondition, javaName);
                    if (dbCriteria != null) {
                        return dbCriteria;
                    }
                }
            }
        }
        return null;
    }

    static String clazzToDamiEntityName(Class<?> clazz) {
        // 比如 AnAnnoMenuProxy 则返回 PrimaryKeyModel.BaseMetaModel.AnAnnoMenu 由最顶层的类名开始
        Class<?> clazzTemp = clazz;
        List<String> list = new ArrayList<>();
        do {
            if (clazzTemp.equals(Object.class)) {
                list.add(0, "all");
                break;
            }
            list.add(0, clazzTemp.getSimpleName());
            clazzTemp = clazzTemp.getSuperclass();
        } while (clazzTemp != null);
        return String.join(".", list);
    }

    /**
     * 顺序位，越小越先执行
     */
    default int index() {
        return 1000;
    }

    /**
     * 增加前
     *
     * @param data 数据
     */
    default void beforeAdd(T data) {

    }

    /**
     * 增加后
     *
     * @param data 数据
     */
    default void afterAdd(T data) {

    }

    /**
     * 在更新之前
     *
     * @param data data
     */
    default void beforeUpdate(T data, DbCriteria criteria) {

    }


    /**
     * 修改后
     *
     * @param data 数据
     */
    default void afterUpdate(T data) {

    }

    /**
     * 在删除之前
     */
    default void beforeDelete(DbCriteria criteria) {

    }

    /**
     * 删除后
     */
    default void afterDelete(DbCriteria criteria) {

    }

    /**
     * 查询前，返回值为：自定义查询条件
     */
    default void beforeFetch(DbCriteria criteria) {

    }

    /**
     * 返回结果后
     *
     * @param page 分页结果数据
     */
    default void afterFetch(DbCriteria criteria, AnnoPage<T> page) {

    }

    /**
     * 实体类
     *
     * @return {@link Class}<{@link T}>
     */
    default Class<?> entityClass() {
        try {
            Class<? extends AnnoBaseProxy> nowClass = this.getClass();
            return (Class<T>) GenericsUtil.getInterfaceGenericsType(nowClass, AnnoBaseProxy.class, 0);
        } catch (Exception e) {
            LoggerFactory.getLogger(this.getClass()).warn("自动代理获取实体类失败：{}，自动设置为:{}，代理可能会失效。", e.getMessage(), this.getClass());
            return this.getClass();
        }
    }
}
