package site.sorghum.anno.db.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 页面参数
 *
 * @author sorghum
 * @since 2023/07/08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageParam {
    /**
     * 页码，从 1 开始
     */
    int page;
    /**
     * 每页条数
     */
    int pageSize;

    public int getOffset() {
        return (page - 1) * pageSize;
    }

    public static PageParam of(int page, int limit) {
        return new PageParam(page, limit);
    }

}
