package site.sorghum.anno._ddl.convert;

import lombok.SneakyThrows;
import org.noear.wood.wrap.ColumnWrap;
import org.noear.wood.wrap.TableWrap;
import site.sorghum.ddl.entity.DdlColumnWrap;
import site.sorghum.ddl.entity.DdlTableWrap;

import java.util.ArrayList;

public class DdlWrapConvert {

    public static DdlTableWrap fromTableWrap(TableWrap table) {
        return new DdlTableWrap(
            table.getName(),
            table.getPks() == null ? new ArrayList<>() : table.getPks(),
            table.getRemarks(),
            table.getColumns() == null ? new ArrayList<>() : table.getColumns().stream().map(DdlWrapConvert::fromColumnWrap).toList()
        );
    }

    @SneakyThrows
    public static DdlColumnWrap fromColumnWrap(ColumnWrap column) {
        return new DdlColumnWrap(
            column.getTable(),
            column.getName(),
            null,
            column.getSize(),
            column.getDigit(),
            column.getIsNullable().equals("YES"),
            null,
            column.getRemarks()
        );
    }
}
