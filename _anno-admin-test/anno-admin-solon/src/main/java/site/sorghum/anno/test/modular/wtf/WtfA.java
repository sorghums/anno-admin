package site.sorghum.anno.test.modular.wtf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoOrder;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.db.BaseMetaModel;

/**
 * 电商商品
 *
 * @author Sorghum
 * @since 2023/07/04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "沃特发表A",
    dbName = "db2Context",
    annoOrder = @AnnoOrder(orderValue = "id", orderType = "desc")
)
@Table("wtf_a")
@AllArgsConstructor
@NoArgsConstructor
public class WtfA extends BaseMetaModel implements WtfCInterfaces {

    @AnnoField(
        title = "名称",
        tableFieldName = "name",
        search = @AnnoSearch,
        edit = @AnnoEdit)
    String name;

    @AnnoField(
        title = "年龄",
        tableFieldName = "age",
        edit = @AnnoEdit)
    String age;

    @AnnoButton(name = "沃特发B",
        o2mJoinButton = @AnnoButton.O2MJoinButton(targetClass = WtfB.class,
            thisJavaField = "id",
            targetJavaField = "wtfA"))
    private Object wtfB;

    /**
     * 位置
     */
    private String location;
}
