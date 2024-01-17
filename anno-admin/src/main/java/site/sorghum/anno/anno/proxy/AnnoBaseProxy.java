package site.sorghum.anno.anno.proxy;

import site.sorghum.anno.anno.entity.common.AnnoPage;
import site.sorghum.anno.db.DbCriteria;
import site.sorghum.anno.method.MethodTemplate;
import site.sorghum.anno.method.route.EntityMethodRoute;

import java.util.ArrayList;
import java.util.List;

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
        return null;
    }

    static String clazzToDamiEntityName(Class<?> clazz) {
        // 比如 AnAnnoMenuProxy 则返回 PrimaryKeyModel.BaseMetaModel.AnAnnoMenu 由最顶层的类名开始
        Class<?> clazzTemp = clazz;
        List<String> list = new ArrayList<>();
        do {
            if (clazzTemp.equals(Object.class)) {
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


}
