package site.sorghum.anno.db.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 表参数
 *
 * @author sorghum
 * @since 2023/07/08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableParam<T> {
    /**
     * 实体类
     */
    Class<T> clazz;
    /**
     * 表名
     */
    String tableName;

    /**
     * 列名
     */
    List<String> columns = new ArrayList<>();

    /**
     * 删除参数
     */
    RemoveParam removeParam;

    /**
     * 排序参数
     */
    OrderByParam orderByParam = new OrderByParam();

    public void addColumn(String column) {
        if (this.columns == null) {
            this.columns = new ArrayList<>();
        }
        this.columns.add(column);
    }

    public String getColumnStr() {
        String column = String.join(",", this.getColumns());
        if (column.length() == 0) {
            column = "*";
        }
        return column;
    }

    /**
     * 设置实体类
     *
     * @param clazz 实体类
     */
    public void setClazz(Class<?> clazz) {
        this.clazz = (Class<T>) clazz;
    }
}
