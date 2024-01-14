package site.sorghum.anno.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 删除参数
 *
 * @author sorghum
 * @since 2023/07/08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DbRemove {
    /**
     * 是否逻辑删除
     */
    Boolean logic = false;

    /**
     * 逻辑::删除字段
     */
    String removeColumn = "del_flag";
    /**
     * 逻辑::删除值
     */
    String removeValue = "1";
    /**
     * 逻辑::非删除值
     */
    String notRemoveValue = "0";
}
