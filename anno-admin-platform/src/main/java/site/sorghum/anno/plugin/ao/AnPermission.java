package site.sorghum.anno.plugin.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.suppose.model.BaseMetaModel;

import java.io.Serializable;

/**
 * 权限管理
 *
 * @author sorghum
 * @since 2023/05/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "权限管理",
        annoTree = @AnnoTree(label = "name", parentKey = "parentId", key = "id", displayAsTree = true),
        annoPermission = @AnnoPermission(enable = true, baseCode = "an_permission", baseCodeTranslate = "权限管理")
)
@Table("an_permission")
public class AnPermission extends BaseMetaModel implements Serializable {

    /**
     * 权限名称
     */
    @AnnoField(title = "权限名称", tableFieldName = "name")
    private String name;

    /**
     * 权限Code
     */
    @AnnoField(title = "权限码", tableFieldName = "code")
    private String code;

    /**
     * 父权限id
     */
    @AnnoField(title = "父权限", tableFieldName = "parent_id",search = @AnnoSearch())
    private String parentId;
}