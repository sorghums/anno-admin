package site.sorghum.anno.modular.anno.service;

import com.alibaba.fastjson2.JSONObject;
import site.sorghum.anno.modular.anno.entity.common.AnnoTreeDto;
import site.sorghum.anno.modular.anno.entity.req.QueryRequest;
import org.noear.wood.IPage;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Anno服务
 *
 * @author sorghum
 * @since 2023/05/20
 */
public interface AnnoService {
    /**
     * 分页查询
     *
     * @param queryRequest 页面请求
     * @return {@link IPage}<{@link T}>
     */
    <T> IPage<T> page(QueryRequest<T> queryRequest);

    /**
     * sql查询
     *
     * @param clazz clazz
     * @param sql   sql
     * @return {@link String}
     */
    <T> String sql(Class<T> clazz,String sql);

    /**
     * m2m sql
     *
     * @param param 参数
     * @return {@link String}
     */
    <T> String m2mSql(Map<?,String > param);

    /**
     * 分页查询
     *
     * @param queryRequest 页面请求
     * @return {@link IPage}<{@link T}>
     */
    <T> List<T> list(QueryRequest<T> queryRequest);

    /**
     * 通过id查询
     *
     * @param clazz clazz
     * @param id    id
     */
    <T> T queryById(Class<T> clazz, Serializable id);

    /**
     * 通过id 删除
     *
     * @param clazz clazz
     * @param id    id
     */
    <T> void removeById(Class<T> clazz, Serializable id);

    /**
     * 通过id 更新
     *
     * @param t t
     */
    <T> void updateById(T t);

    /**
     * 保存
     *
     * @param param 参数
     */
    <T> void save(T param);

    /**
     * 保存
     *
     * @param param 参数
     */
    <T> void onlySave(T param);

    /**
     * Anno 树
     *
     * @param clazz clazz
     * @return {@link List}<{@link AnnoTreeDto}<{@link String}>>
     */
    <T> List<AnnoTreeDto<String>> annoTrees(Class<T> clazz);
}
