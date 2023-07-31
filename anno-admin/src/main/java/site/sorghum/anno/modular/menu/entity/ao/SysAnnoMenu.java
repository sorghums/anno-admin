package site.sorghum.anno.modular.menu.entity.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoProxy;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.modular.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.base.model.BaseMetaModel;
import site.sorghum.anno.modular.menu.entity.proxy.SysAnnoMenuProxy;


/**
 * 系统菜单
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "菜单管理",
        annoTree = @AnnoTree(label = "title", parentKey = "parentId", key = "id", displayAsTree = true),
        annoPermission = @AnnoPermission(enable = true, baseCode = "sys_anno_menu", baseCodeTranslate = "菜单管理"),
annoProxy = @AnnoProxy(value = SysAnnoMenuProxy.class))
@Table("sys_anno_menu")
public class SysAnnoMenu extends BaseMetaModel {

    @AnnoField(title = "父菜单", tableFieldName = "parent_id", edit = @AnnoEdit)
    private String parentId;

    @AnnoField(title = "菜单名称", tableFieldName = "title", edit = @AnnoEdit)
    private String title;

    @AnnoField(title = "菜单类型", tableFieldName = "type", search = @AnnoSearch(),
            dataType = AnnoDataType.OPTIONS,
            optionType = @AnnoOptionType(value = {
                    @AnnoOptionType.OptionData(label = "页面", value = "1"),
                    @AnnoOptionType.OptionData(label = "目录", value = "0")
            }),
            edit = @AnnoEdit(placeHolder = "请选择菜单类型", notNull = true))
    private Integer type;

    @AnnoField(title = "菜单排序", tableFieldName = "sort", edit = @AnnoEdit)
    private Integer sort;

    @AnnoField(title = "打开方式", tableFieldName = "open_type", edit = @AnnoEdit, optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "内置页", value = "_iframe"),
    }), dataType = AnnoDataType.OPTIONS)
    private String openType;

    @AnnoField(title = "菜单图标", tableFieldName = "icon", edit = @AnnoEdit)
    private String icon;

    @AnnoField(title = "菜单链接", tableFieldName = "href", edit = @AnnoEdit)
    private String href;

    @AnnoField(title = "权限标识", tableFieldName = "permission_id", edit = @AnnoEdit,
            dataType = AnnoDataType.OPTIONS,
            optionType = @AnnoOptionType(sql = "select id as value, name as label from sys_permission where del_flag = 0 and parent_id is null order by id desc"))
    private String permissionId;

    @AnnoField(title = "解析类型", tableFieldName = "parse_type", edit = @AnnoEdit,
            dataType = AnnoDataType.OPTIONS,
            optionType =@AnnoOptionType(value = {
                    @AnnoOptionType.OptionData(label = "Anno组件", value = "annoMain")
            }))
    private String parseType;

    @AnnoField(title = "解析数据", tableFieldName = "parse_data", edit = @AnnoEdit)
    private String parseData;
}
