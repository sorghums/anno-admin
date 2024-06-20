package site.sorghum.anno.plugin.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoOrder;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoImageType;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.suppose.model.BaseOrgMetaModel;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "C端管理",
    tableName = "an_client_user",
    annoPermission = @AnnoPermission(enable = true, baseCode = "an_client_user", baseCodeTranslate = "C端管理"),
    annoOrder = {@AnnoOrder(orderType = "desc", orderValue = "id")}
)
public class AnClientUser extends BaseOrgMetaModel implements Serializable {

    /**
     * 用户头像
     */
    @AnnoField(title = "用户头像",
        tableFieldName = "avatar",
        dataType = AnnoDataType.AVATAR,
        edit = @AnnoEdit(placeHolder = "请上传用户头像"),
        imageType = @AnnoImageType(width = 50, height = 50))
    private String avatar;

    /**
     * 手机号
     */
    @AnnoField(title = "手机号", tableFieldName = "mobile", search = @AnnoSearch(),
        edit = @AnnoEdit(placeHolder = "请输入手机号", notNull = true, editEnable = false))
    private String mobile;

    /**
     * 密码
     */
    @AnnoField(title = "密码", tableFieldName = "password",
        edit = @AnnoEdit(editEnable = false), show = false)
    private String password;

    /**
     * 用户名
     */
    @AnnoField(title = "用户名", tableFieldName = "name", search = @AnnoSearch(),
        edit = @AnnoEdit(placeHolder = "请输入用户名", notNull = true))
    private String name;

    /**
     * 状态 1 正常 0 封禁
     */
    @AnnoField(title = "状态", tableFieldName = "enable", search = @AnnoSearch(),
        dataType = AnnoDataType.RADIO,
        optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "正常", value = "1"),
            @AnnoOptionType.OptionData(label = "封禁", value = "0")
        }),
        edit = @AnnoEdit(placeHolder = "请选择状态", notNull = true))
    private String enable;

    @Serial
    private static final long serialVersionUID = 1;
}