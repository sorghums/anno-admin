package site.sorghum.anno.modular.anno.service;

import cn.hutool.core.lang.Tuple;
import org.noear.wood.IPage;
import site.sorghum.anno.modular.anno.entity.common.AnnoTreeDTO;
import site.sorghum.anno.modular.anno.entity.req.QueryRequest;

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
    <T> String m2mSql(Map<?,? > param);

    /**
     * Anno 树
     *
     * @param tClass   t类
     * @param dataList 数据列表
     * @return {@link List}<{@link AnnoTreeDTO}<{@link String}>>
     */
    <T> List<AnnoTreeDTO<String>> annoTrees(Class<T> tClass,List<T> dataList);
}
