package site.sorghum.anno.plugin.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.clazz.AnnoTree;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.suppose.model.BaseMetaModel;


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
        annoPermission = @AnnoPermission(enable = true, baseCode = "an_anno_menu", baseCodeTranslate = "菜单管理"))
@Table("an_anno_menu")
public class AnAnnoMenu extends BaseMetaModel {
    public static class ParseTypeConstant {
        /**
         * anno组件
         */
        public static final String ANNO_MAIN = "annoMain";
        /**
         * iframe
         */
        public static final String IFRAME = "iframe";
        /**
         * 外链
         */
        public static final String LINK = "link";
        /**
         * TPL 服务端页面
         */
        public static final String TPL = "tpl";
    }

    @AnnoField(title = "父菜单", tableFieldName = "parent_id", edit = @AnnoEdit,
        dataType = AnnoDataType.TREE, treeType = @AnnoTreeType(treeAnno = @AnnoTreeType.TreeAnnoClass(annoClass = AnAnnoMenu.class,labelKey = "title",pidKey = "parentId")))
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

    @AnnoField(title = "菜单图标", tableFieldName = "icon", edit = @AnnoEdit, dataType = AnnoDataType.ICON)
    private String icon;

    /**
     * 由解析数据生成
     */
    private String href;

    @AnnoField(title = "权限标识", tableFieldName = "permission_id", edit = @AnnoEdit,
            dataType = AnnoDataType.OPTIONS,
            optionType = @AnnoOptionType(sql = "select id, name as label from an_permission where del_flag = 0 and parent_id is null order by id desc"))
    private String permissionId;

    @AnnoField(title = "解析类型", tableFieldName = "parse_type", edit = @AnnoEdit,
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "Anno组件", value = ParseTypeConstant.ANNO_MAIN),
            @AnnoOptionType.OptionData(label = "IFrame页面", value = ParseTypeConstant.IFRAME),
            @AnnoOptionType.OptionData(label = "外链", value = ParseTypeConstant.LINK),
            @AnnoOptionType.OptionData(label = "TPL模板", value = ParseTypeConstant.TPL)
        }))
    private String parseType;

    @AnnoField(title = "解析数据", tableFieldName = "parse_data", edit = @AnnoEdit)
    private String parseData;
}
