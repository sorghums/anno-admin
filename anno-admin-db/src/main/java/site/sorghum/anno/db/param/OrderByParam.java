package site.sorghum.anno.db.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.noear.wood.DbTableQuery;

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
public class OrderByParam {
    /**
     * 排序项
     */
    List<OrderByItem> orderByItems;


    /**
     * 添加
     *
     * @param orderByItem 按项目
     */
    public void addOrderByItem(OrderByItem orderByItem) {
        if (this.orderByItems == null) {
            this.orderByItems = new ArrayList<>();
        }
        this.orderByItems.add(orderByItem);
    }

    /**
     * 填补sql
     *
     * @param dbTableQuery 数据库表查询
     */
    public void fillSql(DbTableQuery dbTableQuery){
        if (this.orderByItems != null) {
            for (OrderByItem orderByItem : this.orderByItems) {
                if (orderByItem.getColumn() == null) {
                    continue;
                }
                if (orderByItem.isAsc()) {
                    dbTableQuery.orderByAsc(orderByItem.getColumn());
                } else {
                    dbTableQuery.orderByDesc(orderByItem.getColumn());
                }
            }
        }
    }
    /**
     * 排序项
     *
     * @author Sorghum
     * @since 2023/07/10
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderByItem {
        /**
         * 列名
         */
        public String column;

        /**
         * 是否升序
         */
        public boolean asc;

    }
}
