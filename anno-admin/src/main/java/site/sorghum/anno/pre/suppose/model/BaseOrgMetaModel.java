package site.sorghum.anno.pre.suppose.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.anno.annotation.clazz.AnnoPreProxy;
import site.sorghum.anno.anno.annotation.clazz.AnnoRemove;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.pre.suppose.proxy.BaseAnnoPreProxy;

import java.io.Serializable;

/**
 * 雪花模型
 *
 * @author Sorghum
 * @since 2023/03/08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoPreProxy(BaseAnnoPreProxy.class)
@AnnoRemove(removeType = 1)
public class BaseOrgMetaModel extends BaseMetaModel implements Serializable {

    @AnnoField(title = "组织",
            tableFieldName = "org_id", edit = @AnnoEdit(),
            dataType = AnnoDataType.OPTIONS,
            search = @AnnoSearch(),
            optionType = @AnnoOptionType(sql = "select id as  value, org_name as label from sys_org where del_flag = 0 order by id desc"))
    private String orgId;
}