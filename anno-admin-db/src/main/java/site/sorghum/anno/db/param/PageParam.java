package site.sorghum.anno.db.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 页面参数
 *
 * @author sorghum
 * @date 2023/07/08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageParam {
    /**
     * 页码
     */
    int page;
    /**
     * 每页条数
     */
    int limit;
}
