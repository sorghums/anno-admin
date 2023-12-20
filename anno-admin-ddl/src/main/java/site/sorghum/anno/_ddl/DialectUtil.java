package site.sorghum.anno._ddl;

import cn.hutool.core.util.StrUtil;
import com.github.drinkjava2.jdialects.model.ColumnModel;
import com.github.drinkjava2.jdialects.model.TableModel;
import org.noear.wood.wrap.ColumnWrap;
import org.noear.wood.wrap.TableWrap;

import java.util.ArrayList;
import java.util.List;

/**
 * 方言
 *
 * @author Sorghum
 * @since 2023/12/20
 */
public class DialectUtil {
    public static TableModel tableWrap2TableModel(TableWrap tableWrap) {
        TableModel tableModel = new TableModel();
        tableModel.setTableName(tableWrap.getName());
        tableModel.setComment(tableWrap.getRemarks());
        for (ColumnWrap column : tableWrap.getColumns()) {
            ColumnModel columnModel = new ColumnModel(column.getName()).comment(column.getRemarks());
            columnModel.setColumnType(TypeEnum.getType(column.getSqlType()));
            if (StrUtil.equalsIgnoreCase(column.getName(),tableWrap.getPk1())){
                columnModel.setPkey(true);
            }
            columnModel.setLength(column.getSize());
            tableModel.addColumn(columnModel);
        }
        return tableModel;
    }

    public static List<ColumnModel> columnWrap2ColumnModel(List<ColumnWrap> columnWrap, TableWrap tableWrap) {
        TableModel tableModel = tableWrap2TableModel(tableWrap);
        ArrayList<ColumnModel> columnModels = new ArrayList<>();
        for (ColumnWrap column : columnWrap) {
            ColumnModel columnModel = new ColumnModel(column.getName()).comment(column.getRemarks());
            columnModel.setColumnType(TypeEnum.getType(column.getSqlType()));
            if (StrUtil.equalsIgnoreCase(column.getName(),tableWrap.getPk1())){
                columnModel.setPkey(true);
            }
            columnModel.setLength(column.getSize());
            columnModel.setTableModel(tableModel);
            columnModels.add(columnModel);
        }
        return columnModels;
    }
}
