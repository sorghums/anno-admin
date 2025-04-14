package site.sorghum.anno.om.ao;

import lombok.Data;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.om.supplier.DsNameSupplier;
import site.sorghum.plugin.join.aop.JoinResMap;

import java.util.HashMap;
import java.util.Map;

@AnnoMain(
    name = "在线数据表",
    tableName = "an_online_table",
    virtualTable = true
)
@Data
public class OnlineTable {
    @AnnoField(
        title = "主键",
        pkField = true,
        fieldSize = 256
    )
    String id;

    @AnnoField(
        title = "数据源名称",
        search = @AnnoSearch(enable = true, placeHolder = "请输入数据源名称", notNull = true),
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(
            supplier = DsNameSupplier.class
        ),
        fieldSize = 256
    )
    String dsName;

    @AnnoField(
        title = "数据表名称",
        search = @AnnoSearch(enable = true, placeHolder = "请输入数据表名称"),
        fieldSize = 256
    )
    String tableName;

    @JoinResMap
    Map<String, Object> joinResMap = new HashMap<>();
}
