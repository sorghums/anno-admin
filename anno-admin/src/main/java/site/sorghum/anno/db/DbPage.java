package site.sorghum.anno.db;

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
public class DbPage {
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

    public static DbPage of(int page, int pageSize) {
        return new DbPage(page, pageSize);
    }

}
