package site.sorghum.anno.test.modular.wtf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
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
@AnnoMain(name = "沃特发表C")
@Table("wtf_c")
@NoArgsConstructor
@AllArgsConstructor
public class WtfC extends BaseMetaModel {

    @AnnoField(
            title = "地址",
            tableFieldName = "location",
            search = @AnnoSearch,
            edit = @AnnoEdit)
    String location;

    @AnnoField(title = "沃特发B",
        tableFieldName = "wtf_b", edit = @AnnoEdit(notNull = true),
        dataType = AnnoDataType.CLASS_OPTIONS,
        search = @AnnoSearch(),
        optionType = @AnnoOptionType(
            optionAnno = @AnnoOptionType.OptionAnnoClass(annoClass = WtfB.class,labelKey = "attr")
        ))
    String wtfB;
    
}
