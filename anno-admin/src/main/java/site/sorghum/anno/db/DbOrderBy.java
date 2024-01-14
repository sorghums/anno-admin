package site.sorghum.anno.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.noear.wood.DbTableQuery;
import site.sorghum.anno._metadata.AnOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * 排序参数
 *
 * @author Sorghum
 * @since 2023/07/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DbOrderBy {
    /**
     * 排序项
     */
    List<OrderByItem> orderByItems;


    /**
     * 升序
     *
     * @param column 列名
     */
    public void asc(String column) {
        orderBy("ASC", column);
    }

    /**
     * 降序
     *
     * @param column 列名
     */
    public void desc(String column) {
        orderBy("DESC", column);
    }

    public void orderBy(String orderType, String column) {
        if (this.orderByItems == null) {
            this.orderByItems = new ArrayList<>();
        }
        this.orderByItems.add(new OrderByItem(orderType, column));
    }

    /**
     * 排序项
     *
     * @author Sorghum
     * @since 2023/07/10
     */
    @Data
    @NoArgsConstructor
    public static class OrderByItem {

        public OrderByItem(String orderType, String column) {
            this.orderType = orderType;
            this.column = column;
        }

        /**
         * 排序类型 asc, desc
         */
        public String orderType;
        /**
         * 列名
         */
        public String column;

    }
}
