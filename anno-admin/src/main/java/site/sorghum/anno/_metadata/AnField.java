package site.sorghum.anno._metadata;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoImageType;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.db.param.DbCondition;

import java.util.List;

/**
 * @author songyinyin
 * @see AnnoField
 * @since 2023/7/9 21:21
 */
@Data
public class AnField {

    /**
     * 字段名
     */
    @ApiModelProperty(value = "字段名",example = "userName")
    private String fieldName;

    /**
     * 虚拟列
     * @see AnnoField#virtualColumn()
     */
    @ApiModelProperty(value = "虚拟列",example = "false")
    private boolean virtualColumn = false;

    /**
     * 标题
     *
     * @see AnnoField#title()
     */
    @ApiModelProperty(value = "标题",example = "用户名")
    private String title;

    /**
     * 表字段名
     *
     * @see AnnoField#tableFieldName()
     */
    @ApiModelProperty(value = "表字段名",example = "user_name")
    private String tableFieldName;

    /**
     * 是否主键
     */
    @ApiModelProperty(value = "是否主键",example = "false")
    private boolean primaryKey;

    /**
     * 字段类型
     */
    @ApiModelProperty(value = "字段类型",example = "java.lang.String")
    private Class<?> fieldType;

    /**
     * 数据库中的字段长度，为 0 时使用 anno 设置的默认长度
     *
     * @see AnnoField#fieldSize()
     */
    @ApiModelProperty(value = "数据库中的字段长度，为 0 时使用 anno 设置的默认长度",example = "0")
    private int fieldSize;

    /**
     * 定义数据库中的默认值，比如：DEFAULT 0
     *
     * @see AnnoField#defaultValue()
     */
    @ApiModelProperty(value = "定义数据库中的默认值，比如：DEFAULT 0",example = "DEFAULT 0")
    private String defaultValue;

    /**
     * 显示
     *
     * @see AnnoField#show()
     */
    @ApiModelProperty(value = "显示[是否在Table中显示]",example = "true")
    private boolean show;

    /**
     * 搜索 是否开启
     *
     * @see AnnoSearch#enable()
     */
    @ApiModelProperty(value = "搜索[是否在QueryForm中显示，支持搜索]",example = "true")
    private boolean searchEnable;

    /**
     * 搜索 是否必填
     *
     * @see AnnoSearch#notNull()
     */
    @ApiModelProperty(value = "搜索时是否必填",example = "false")
    private boolean searchNotNull;

    /**
     * 搜索 查询类型
     *
     * @see AnnoSearch#queryType()
     */
    @ApiModelProperty(value = "搜索时查询类型",example = "EQ")
    private DbCondition.QueryType searchQueryType;

    /**
     * 搜索 提示信息
     *
     * @see AnnoSearch#placeHolder()
     */
    @ApiModelProperty(value = "搜索时的提示信息[前端的placeholder]",example = "请输入用户名")
    private String searchPlaceHolder;

    /**
     * 搜索框 大小
     *
     * @see AnnoSearch#size()
     */
    @ApiModelProperty(value = "搜索框大小",example = "default")
    private String searchSize;

    /**
     * 编辑 是否开启
     *
     * @see AnnoEdit#editEnable()
     */
    @ApiModelProperty(value = "编辑[是否在EditForm中显示，支持编辑]",example = "true")
    private boolean editEnable;

    /**
     * 编辑 是否必填
     *
     * @see AnnoEdit#notNull()
     */
    @ApiModelProperty(value = "编辑时是否必填",example = "false")
    private boolean editNotNull;

    /**
     * 编辑 提示信息
     *
     * @see AnnoEdit#placeHolder()
     */
    @ApiModelProperty(value = "编辑时的提示信息[前端的placeholder]",example = "请输入用户名")
    private String editPlaceHolder;

    /**
     * 编辑跨度
     * 默认为 24 [1-24] 24为整行
     *
     */
    @ApiModelProperty(value = "编辑跨度[1-24] 24为整行",example = "24")
    private int editSpan;

    /**
     * 编辑 新增启用
     *
     * @see AnnoEdit#addEnable()
     */
    @ApiModelProperty(value = "编辑[是否在AddForm中显示，支持新增]",example = "true")
    private boolean addEnable;

    /**
     * 编辑/新增时候的依赖v-show展示逻辑
     *
     * @see AnnoEdit#showBy()
     */
    @ApiModelProperty(value = "编辑/新增时候的依赖v-show展示逻辑")
    private List<EditShowBy> editShowBy;

    @Data
    public static class EditShowBy {

        /**
         * 展示条件
         */
        @ApiModelProperty(value = "展示条件")
        List<EditShowIf> showIf;

        /**
         * 与或
         */
        @ApiModelProperty(value = "与或",example = "and")
        private String andOr;

        /**
         * 是否启用
         */
        @ApiModelProperty(value = "是否启用",example = "true")
        private Boolean enable;
    }

    @Data
    public static class EditShowIf {
        /**
         * 依赖字段
         */
        @ApiModelProperty(value = "依赖字段",example = "productName")
        private String dependField;

        /**
         * 表达式
         */
        @ApiModelProperty(value = "表达式",example = "value!= ''")
        private String expr;

        /**
         * 与或
         */
        @ApiModelProperty(value = "与或",example = "and")
        private String andOr;
    }

    /**
     * 数据类型
     *
     * @see AnnoField#dataType()
     */
    @ApiModelProperty(value = "数据类型",example = "STRING",allowableValues = "STRING,FILE,IMAGE,NUMBER,DATE,DATETIME,OPTIONS,PICKER,TREE,RICH_TEXT,CODE_EDITOR")
    private AnnoDataType dataType;

    /**
     * 查询Sql
     *
     * @see AnnoOptionType#sql()
     */
    @ApiModelProperty(value = "[下拉框]查询Sql的Key",example = "a123456")
    private String optionTypeSql;

    /**
     * annoMain的类自动解析Sql
     *
     * @see AnnoOptionType#optionAnno()
     */
    @ApiModelProperty(value = "[下拉框]annoMain的自动解析Sql类")
    private OptionAnnoClass optionAnnoClass;

    /**
     * 是否多选，多选的值格式为逗号拼接 value 值
     */
    @ApiModelProperty(value = "[下拉框]是否多选，多选的值格式为逗号拼接 value 值",example = "false")
    private boolean optionIsMultiple;

    @Data
    @AllArgsConstructor
    public static class OptionAnnoClass {
        /**
         * 显示的标签 key
         */
        @ApiModelProperty(value = "显示的标签 key",example = "name")
        String labelKey;

        /**
         * 显示的值 key
         */
        @ApiModelProperty(value = "显示的值 key",example = "id")
        String idKey;

        /**
         * annoMain注释的类，比如 SysOrg.class
         * 最后会执行类似的：select value, label from sys_org where del_flag = 0 order by id desc
         * 并且会自动走SysOrg的代理操作
         */
        @ApiModelProperty(value = "annoMain注释的类，比如 SysOrg.class",example = "site.sorghum.anno.plugin.ao.SysOrg")
        Class<?> annoClass;
    }
    /**
     * 选择数据
     *
     * @see AnnoOptionType.OptionData
     */
    @ApiModelProperty(value = "[下拉框]选择数据[直接固定的数据]")
    private List<OptionData> optionDatas;

    @Data
    @AllArgsConstructor
    public static class OptionData {

        /**
         * 页面显示的内容
         *
         * @see AnnoOptionType.OptionData#label()
         */
        @ApiModelProperty(value = "标签",example = "正常")
        private String label;

        /**
         * 传递给后端的值
         *
         * @see AnnoOptionType.OptionData#value()
         */
        @ApiModelProperty(value = "值",example = "1")
        private String value;
    }

    /**
     * 图像 预览图比率
     *
     * @see AnnoImageType#thumbRatio()
     */
    @ApiModelProperty(value = "[图片]预览图比率",example = "RATE_ONE",allowableValues = "RATE_ONE,RATE_TWO,RATE_THREE")
    private AnnoImageType.ThumbRatio imageThumbRatio;

    /**
     * 图像 图片模式
     *
     * @see AnnoImageType#thumbMode()
     */
    @ApiModelProperty(value = "[图片]图片模式",example = "DEFAULT",allowableValues = "DEFAULT,COVER,W_FULL,H_FULL")
    private AnnoImageType.ThumbMode imageThumbMode;

    /**
     * 图像 点击可放大展示
     *
     * @see AnnoImageType#enlargeAble()
     */
    @ApiModelProperty(value = "[图片]点击可放大展示",example = "true")
    private boolean imageEnlargeAble;

    /**
     * 图像 宽度 px
     *
     * @see AnnoImageType#width()
     */
    @ApiModelProperty(value = "[图片]宽度 px",example = "0")
    private int imageWidth;

    /**
     * 图像 高度 px
     *
     * @see AnnoImageType#height()
     */
    @ApiModelProperty(value = "[图片]高度 px",example = "0")
    private int imageHeight;

    /**
     * 选择类型-树形
     *
     * @see AnnoTreeType#sql()
     */
    @ApiModelProperty(value = "[树形选择框]查询Sql的Key",example = "a123456")
    private String treeTypeSql;

    /**
     * 选择类型-树形
     *
     * @see AnnoTreeType#value()
     */
    @ApiModelProperty(value = "[树形选择框]查询Sql的Key",example = "a123456")
    private List<TreeData> treeDatas;

    /**
     * 选择类型-树形
     * @see AnnoTreeType#treeAnno();
     */
    @ApiModelProperty(value = "[树形选择框]annoMain注释的类，比如 SysOrg.class")
    private TreeAnnoClass treeOptionAnnoClass;

    @Data
    @AllArgsConstructor
    public static class TreeData {

        /**
         * 节点id
         *
         * @see AnnoTreeType.TreeData#id()
         */
        @ApiModelProperty(value = "节点id",example = "1")
        private String id;

        /**
         * 页面显示的内容
         *
         * @see AnnoTreeType.TreeData#label()
         */
        @ApiModelProperty(value = "标签",example = "正常")
        private String label;

        /**
         * 传递给后端的值
         *
         * @see AnnoTreeType.TreeData#value()
         */
        @ApiModelProperty(value = "传递给后端的值",example = "1")
        private String value;

        /**
         * 父节点id
         *
         * @see AnnoTreeType.TreeData#pid()
         */
        @ApiModelProperty(value = "父节点id",example = "0")
        private String pid;
    }


    @Data
    @AllArgsConstructor
    public static class TreeAnnoClass {

        /**
         * 并且会自动解析类信息
         * 且走代理操作
         */
        @ApiModelProperty(value = "annoMain注释的类，比如 SysOrg.class",example = "site.sorghum.anno.plugin.ao.SysOrg")
        Class<?> annoClass;

        /**
         * 显示的值 key
         */
        @ApiModelProperty(value = "显示的值 key",example = "id")
        String idKey;

        /**
         * 显示的标签 key
         */
        @ApiModelProperty(value = "显示的标签 key",example = "name")
        String labelKey;

        /**
         * 父主键 key
         */
        @ApiModelProperty(value = "父主键 key",example = "pid")
        String pidKey;
    }
}
