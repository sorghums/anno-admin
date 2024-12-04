package site.sorghum.anno._ddl;

import cn.hutool.core.util.StrUtil;
import com.github.drinkjava2.jdialects.Type;
import com.github.drinkjava2.jdialects.model.ColumnModel;
import com.github.drinkjava2.jdialects.model.TableModel;
import org.noear.wood.wrap.ColumnWrap;
import org.noear.wood.wrap.TableWrap;

import java.util.ArrayList;
import java.util.Collection;
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
        Collection<ColumnWrap> columns = tableWrap.getColumns();
        for (ColumnWrap column : columns) {
            ColumnModel columnModel = getColumnModel(tableModel, tableWrap, column);
            tableModel.addColumn(columnModel);
        }
        return tableModel;
    }

    public static List<ColumnModel> columnWrap2ColumnModel(List<ColumnWrap> columnWrap, TableWrap tableWrap) {
        TableModel tableModel = tableWrap2TableModel(tableWrap);
        ArrayList<ColumnModel> columnModels = new ArrayList<>();
        for (ColumnWrap column : columnWrap) {
            ColumnModel columnModel = getColumnModel(tableModel, tableWrap, column);
            columnModels.add(columnModel);
        }
        return columnModels;
    }

    private static void notNullOrDefault(ColumnModel columnModel, ColumnWrap colWrapper) {
        // TODO 临时为Wood相关兼容处理.
        String defaultValue = colWrapper.getIsNullable();
        if (StrUtil.isBlank(defaultValue)) {
            return;
        }
        boolean hasDefault = StrUtil.containsAnyIgnoreCase(defaultValue, "default");
        if (hasDefault) {
            defaultValue = StrUtil.replaceIgnoreCase(
                defaultValue,
                "default",
                ""
            );
            columnModel.setDefaultValue(defaultValue);
        } else {
            boolean notNull = StrUtil.containsAnyIgnoreCase(defaultValue, "null", "not");
            if (notNull) {
                columnModel.setNullable(false);
            }else {
                columnModel.setDefaultValue(defaultValue);
            }
        }
    }


    private static ColumnModel getColumnModel(TableModel tableModel, TableWrap tableWrap, ColumnWrap column) {
        ColumnModel columnModel = new ColumnModel("%s".formatted(column.getName())).comment(column.getRemarks());
        columnModel.setColumnType(TypeEnum.getType(column.getSqlType()));
        if (StrUtil.equalsIgnoreCase(column.getName(), tableWrap.getPk1())){
            columnModel.setPkey(true);
        }
        if (columnModel.getColumnType().equals(Type.NUMERIC)){
            if (column.getSize() != null) {
                columnModel.setScale(column.getSize());
            }
            if (column.getDigit() != null) {
                columnModel.setPrecision(column.getDigit());
            }
        }else{
            columnModel.setLength(column.getSize());
        }
        if (tableModel != null) {
            columnModel.setTableModel(tableModel);
        }
        notNullOrDefault(columnModel, column);
        return columnModel;
    }

}
