package site.sorghum.anno.modular.menu.entity.model;

import com.alibaba.fastjson2.annotation.JSONField;
import site.sorghum.anno.modular.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.modular.anno.annotation.field.AnnoField;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.modular.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.modular.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.modular.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.modular.anno.enums.AnnoDataType;
import site.sorghum.anno.modular.system.base.BaseMetaModel;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Anno菜单
 *
 * @author Sorghum
 * @since 2023/05/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AnnoMain(name = "菜单信息", tableName = "sys_anno_menu",
        annoTree = @AnnoTree(label = "title", parentKey = "parentId", key = "id", displayAsTree = true))
public class AnnoMenu extends BaseMetaModel {

    @JSONField(name = "parentId")
    @AnnoField(title = "父菜单id", tableFieldName = "parent_id",edit = @AnnoEdit)
    private String parentId;

    @AnnoField(title = "菜单名称", tableFieldName = "title",edit = @AnnoEdit)
    @JSONField(name = "title")
    private String title;

    @AnnoField(title = "菜单类型", tableFieldName = "type", search = @AnnoSearch(),
            dataType = AnnoDataType.OPTIONS,
            optionType = @AnnoOptionType(value = {
                    @AnnoOptionType.OptionData(label = "页面", value = "1"),
                    @AnnoOptionType.OptionData(label = "目录", value = "0")
            }),
            edit = @AnnoEdit(placeHolder = "请选择菜单类型", notNull = true))
    @JSONField(name = "type")
    private Integer type;

    @AnnoField(title = "菜单排序", tableFieldName = "sort",edit = @AnnoEdit)
    @JSONField(name = "sort")
    private Integer sort;

    @AnnoField(title ="打开方式", tableFieldName = "open_type",edit = @AnnoEdit,optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "内置页", value = "_iframe"),
    }),dataType = AnnoDataType.OPTIONS)
    @JSONField(name = "openType")
    private String openType;

    @AnnoField(title = "菜单图标", tableFieldName = "icon",edit = @AnnoEdit)
    @JSONField(name = "icon")
    private String icon;

    @AnnoField(title = "菜单链接", tableFieldName = "href",edit = @AnnoEdit)
    @JSONField(name = "href")
    private String href;
}
