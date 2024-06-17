package site.sorghum.anno.test.modular.wtf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.db.BaseMetaModel;

/**
 * 电商商品
 *
 * @author Sorghum
 * @since 2023/07/04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "沃特发表B")
@Table("wtf_b")
@AllArgsConstructor
@NoArgsConstructor
public class WtfB extends BaseMetaModel {

    @AnnoField(
            title = "性格",
            tableFieldName = "attr",
            search = @AnnoSearch,
            edit = @AnnoEdit)
    String attr;

    @AnnoField(title = "沃特发A",
        tableFieldName = "wtf_a",
        edit = @AnnoEdit(notNull = true),
        dataType = AnnoDataType.CLASS_OPTIONS,
        search = @AnnoSearch(),
        optionType = @AnnoOptionType(
            optionAnno = @AnnoOptionType.OptionAnnoClass(annoClass = WtfA.class,labelKey = "name")
        ))
    String wtfA;


    @AnnoButton(name = "沃特发C",
        o2mJoinButton = @AnnoButton.O2MJoinButton(targetClass = WtfC.class,
            thisJavaField = "id",
            targetJavaField = "wtfB"))
    private Object wtfC;
    
}
