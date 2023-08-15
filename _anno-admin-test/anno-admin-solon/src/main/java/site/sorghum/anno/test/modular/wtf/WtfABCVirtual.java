package site.sorghum.anno.test.modular.wtf;

import lombok.Data;
import org.noear.wood.annotation.PrimaryKey;
import site.sorghum.anno.anno.annotation.clazz.AnnoJoinTable;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoPreProxy;
import site.sorghum.anno.anno.annotation.clazz.AnnoRemove;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;

/**
 * 复杂连表
 *
 * @author Sorghum
 * @since 2023/07/04
 */
@Data
@AnnoPreProxy(value = WtfABCVirtualProxy.class)
@AnnoMain(name = "沃特发表ABC", virtualTable = true,
    annoJoinTable = @AnnoJoinTable(mainTable = "wtf_c", mainAlias = "t1", joinTables = {
        @AnnoJoinTable.JoinTable(table = "wtf_b", alias = "t2", joinCondition = "t1.wtf_b = t2.id and t2.del_flag = 0", joinType = 1),
        @AnnoJoinTable.JoinTable(table = "wtf_a", alias = "t3", joinCondition = "t2.wtf_a = t3.id and t3.del_flag = 0", joinType = 1)
    }))
/*
 * 上面的其实就是：
 * SELECT `name`, `age`, t2.attr, `location`, t1.del_flag
 * FROM `wtf_c` as t1
 *          LEFT JOIN `wtf_b` as t2 ON t1.wtf_b = t2.id and t2.del_flag = 0
 *          LEFT JOIN `wtf_a` as t3 ON t2.wtf_a = t3.id and t3.del_flag = 0
 * WHERE t1.del_flag = ?
 * LIMIT ?,?
 */
@AnnoRemove(removeType = 1,removeField = "t1.del_flag")
public class WtfABCVirtual{
    @AnnoField(
        title = "主键",
        tableFieldName = "t1.id",
        search = @AnnoSearch)
    @PrimaryKey
    String id;

    @AnnoField(
        title = "名称",
        tableFieldName = "name",
        search = @AnnoSearch)
    String name;

    @AnnoField(
        title = "年龄",
        tableFieldName = "age",
        search = @AnnoSearch)
    String age;

    @AnnoField(
        title = "性格",
        tableFieldName = "t2.attr",
        search = @AnnoSearch)
    String attr;

    @AnnoField(
        title = "地址",
        tableFieldName = "location",
        search = @AnnoSearch)
    String location;

}
