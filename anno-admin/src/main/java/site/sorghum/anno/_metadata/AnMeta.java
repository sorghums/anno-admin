package site.sorghum.anno._metadata;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import site.sorghum.anno.anno.annotation.clazz.AnnoMainImpl;
import site.sorghum.anno.anno.annotation.clazz.AnnoRemoveImpl;
import site.sorghum.anno.anno.annotation.clazz.AnnoTableButtonImpl;
import site.sorghum.anno.anno.annotation.field.AnnoButtonImpl;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AnMeta extends AnnoMainImpl {
    /**
     * 当前类
     */
    private Class<?> thisClass;

    /**
     * 实体名称
     */
    private String entityName;

    /**
     * 拓展类
     */
    private Class<?> extend;

    /**
     * 列信息
     */
    private List<AnField> columns;

    /**
     * 实体主键字段
     */
    private AnField pkColumn;

    /**
     * 按钮信息
     */
    private List<AnnoButtonImpl> columnButtons;

    /**
     * 表级按钮信息
     */
    private List<AnnoTableButtonImpl> tableButtons;

    /**
     * 逻辑删除信息
     */
    private AnnoRemoveImpl annoRemove = new AnnoRemoveImpl();

    public List<AnField> getColumns() {
        if(columns == null) {
            return columns = new java.util.ArrayList<>();
        }
        return columns;
    }

    public List<AnnoButtonImpl> getColumnButtons() {
        if(columnButtons == null) {
            return columnButtons = new java.util.ArrayList<>();
        }
        return columnButtons;
    }


    public List<AnnoTableButtonImpl> getTableButtons() {
        if(tableButtons == null) {
            return tableButtons = new java.util.ArrayList<>();
        }
        return tableButtons;
    }
}
