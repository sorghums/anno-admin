package site.sorghum.anno.suppose.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.anno.annotation.clazz.AnnoRemove;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.plugin.ao.AnOrg;

import java.io.Serializable;

/**
 * 雪花模型
 *
 * @author Sorghum
 * @since 2023/03/08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoRemove(removeType = 1)
public class BaseOrgMetaModel extends BaseMetaModel implements Serializable {

    @AnnoField(title = "组织",
            tableFieldName = "org_id", edit = @AnnoEdit(notNull = true),
            dataType = AnnoDataType.PICKER,
            search = @AnnoSearch,
            optionType = @AnnoOptionType(
                optionAnno = @AnnoOptionType.OptionAnnoClass(annoClass = AnOrg.class,labelKey = "orgName")
            ))
    private String orgId;
}
