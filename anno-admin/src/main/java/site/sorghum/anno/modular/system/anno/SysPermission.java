package site.sorghum.anno.modular.system.anno;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.modular.anno.annotation.field.AnnoButton;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.base.model.BaseMetaModel;

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
        annoPermission = @AnnoPermission(enable = true, baseCode = "sys_permission", baseCodeTranslate = "权限管理")
)
@Table("sys_permission")
public class SysPermission extends BaseMetaModel implements Serializable {

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
    @AnnoField(title = "父权限", tableFieldName = "parent_id",
            dataType = AnnoDataType.OPTIONS,
            optionType = @AnnoOptionType(sql = "select id as value,name as label from sys_permission where del_flag = 0 order by id desc"))
    private String parentId;

    @AnnoButton(name = "跳去百度", jumpUrl = "https://www.baidu.com/?tn=${clazz}&props=${props}")
    private Object jump2BaiduButton;

    @AnnoButton(name = "简单的JS命令", jsCmd = "alert('点击了按钮'); console.log(props);")
    private Object jsCmd;
}