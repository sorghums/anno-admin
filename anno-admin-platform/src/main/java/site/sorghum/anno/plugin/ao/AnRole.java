package site.sorghum.anno.plugin.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoOrder;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.clazz.AnnoProxy;
import site.sorghum.anno.anno.annotation.field.AnnoButton;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.plugin.proxy.AnRoleProxy;
import site.sorghum.anno.suppose.model.BaseMetaModel;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "角色管理",
        annoPermission = @AnnoPermission(enable = true, baseCode = "an_role", baseCodeTranslate = "角色管理"),
        annoProxy = @AnnoProxy(value = AnRoleProxy.class),
        annoOrder = @AnnoOrder(orderType = "asc", orderValue = "sort"))
@Table("an_role")
public class AnRole extends BaseMetaModel implements Serializable {

    /**
     * 角色名称
     */
    @AnnoField(title = "角色名称", tableFieldName = "role_name", search = @AnnoSearch(),
            edit = @AnnoEdit(placeHolder = "请输入角色名称", notNull = true))
    String roleName;

    /**
     * 排序
     */
    @AnnoField(title = "排序", tableFieldName = "sort",
            edit = @AnnoEdit(placeHolder = "请输入排序", notNull = true))
    Integer sort;

    /**
     * 状态 1 正常 0 封禁
     */
    @AnnoField(title = "状态", tableFieldName = "enable", search = @AnnoSearch(),
            dataType = AnnoDataType.OPTIONS,
            optionType = @AnnoOptionType(value = {
                    @AnnoOptionType.OptionData(label = "正常", value = "1"),
                    @AnnoOptionType.OptionData(label = "封禁", value = "0")
            }),
            edit = @AnnoEdit(placeHolder = "请选择状态", notNull = true))
    Integer enable;

    /**
     * 用户按钮
     */
    @AnnoButton(name = "用户", m2mJoinButton = @AnnoButton.M2MJoinButton(
            joinAnnoMainClazz = AnUser.class,
            mediumTableClass = AnUserRole.class,
            mediumOtherField = "user_id",
            mediumThisField = "role_id",
            joinThisClazzField = "id"
    ))
    private Object userButton;


    /**
     * 权限按钮
     */
    @AnnoButton(name = "权限", m2mJoinButton = @AnnoButton.M2MJoinButton(
            joinAnnoMainClazz = AnPermission.class,
            mediumTableClass = AnRolePermission.class,
            mediumOtherField = "permission_id",
            mediumThisField = "role_id",
            joinThisClazzField = "id",
            windowSize = "sm"
    ))
    private Object roleButton;
}