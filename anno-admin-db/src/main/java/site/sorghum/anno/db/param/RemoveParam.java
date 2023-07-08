package site.sorghum.anno.db.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 删除参数
 *
 * @author sorghum
 * @date 2023/07/08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveParam {
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
