package site.sorghum.anno.modular.anno.entity.req;
import lombok.Data;

/**
 * 页面请求
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Data
public class QueryRequest<T> {
    /**
     * Anno 类名
     */
    Class<T> clazz;
    /**
     * 页码
     */
    int page;
    /**
     * 每页大小
     */
    int perPage;
    /**
     * 查询参数
     */
    T param;
    /**
     * 排序字段
     */
    String orderBy;

    /**
     * 分类
     */
    String cat;

    /**
     * 额外的sql
     */
    String andSql;

    /**
     * 排序方向,asc,desc
     */
    String orderDir;

    /**
     * 是否是正序
     *
     * @return boolean
     */
    public boolean isAsc() {
        return "asc".equalsIgnoreCase(orderDir);
    }
}
