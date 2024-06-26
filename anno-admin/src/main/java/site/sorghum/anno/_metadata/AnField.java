package site.sorghum.anno._metadata;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoFileType;
import site.sorghum.anno.anno.annotation.field.type.AnnoImageType;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.option.OptionDataSupplier;
import site.sorghum.anno.anno.proxy.field.FieldBaseSupplier;
import site.sorghum.anno.anno.tree.TreeDataSupplier;
import site.sorghum.anno.db.QueryType;

import java.lang.reflect.Field;
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
     * 反射的Field对象
     */
    @ApiModelProperty(value = "反射的Field对象",example = "site.sorghum.anno.suppose.model.BaseMetaModel#userName")
    private Field reflectField;

    /**
     * 字段所在的类。父类上的字段，为父类的类
     */
    @ApiModelProperty(value = "字段所在的类", example = "site.sorghum.anno.suppose.model.BaseMetaModel")
    private Class<?> declaringClass;

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
    private QueryType searchQueryType;

    /**
     * 搜索 提示信息
     *
     * @see AnnoSearch#placeHolder()
     */
    @ApiModelProperty(value = "搜索时的提示信息[前端的placeholder]",example = "请输入用户名")
    private String searchPlaceHolder;

    /**
     * 编辑 是否开启
     *
     * @see AnnoEdit#editEnable()
     */
    @ApiModelProperty(value = "编辑[是否在EditForm中显示，支持编辑]",example = "true")
    private boolean editEnable;

    /**
     * 编辑可以清除
     */
    @ApiModelProperty(value = "编辑[是否可以清空,设置为null]",example = "false")
    private boolean editCanClear;

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
    private EditShowBy editShowBy;

    @Data
    public static class EditShowBy {

        @ApiModelProperty(value = "解析语句",example = "annoDataForm.id != 0")
        private String expr;

        /**
         * 是否启用
         */
        @ApiModelProperty(value = "是否启用",example = "true")
        private Boolean enable;
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
     * 自动解析枚举类
     *
     * @see AnnoOptionType#optionEnum()
     */
    @ApiModelProperty(value = "[下拉框]自动解析枚举类")
    private Class<? extends Enum> optionEnum;


    /**
     * 选项供应商
     *
     * @see AnnoOptionType#supplier
     */
    @ApiModelProperty(value = "[下拉框]选项供应商")
    public Class<? extends OptionDataSupplier> optionSupplier;

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
     * 树形数据供应商
     *
     * @see AnnoTreeType#supplier()
     */
    private Class<? extends TreeDataSupplier> treeOptionSupplier;

    /**
     * 选择类型-树形
     * @see AnnoTreeType#treeAnno();
     */
    @ApiModelProperty(value = "[树形选择框]annoMain注释的类，比如 SysOrg.class")
    private TreeAnnoClass treeOptionAnnoClass;

    /**
     * 是否多选，多选的值格式为逗号拼接 value 值
     */
    @ApiModelProperty(value = "[树形选择框]是否多选，多选的值格式为逗号拼接 value 值",example = "false")
    private boolean treeOptionIsMultiple;

    /**
     * 文件类型限制
     *
     * @see AnnoFileType#fileType()
     */
    @ApiModelProperty(value = "[文件类型限制]默认为*,多种格式以,分格",example = "pdf,jpg")
    private String fileType;

    /**
     * 文件最大上传数量限制
     * @see AnnoFileType#fileMaxCount()
     */
    @ApiModelProperty(value = "[文件最大上传数量限制]默认为1",example = "1")
    private int fileMaxCount;

    /**
     * 文件最大上传大小限制
     * @see AnnoFileType#fileMaxSize()
     */
    @ApiModelProperty(value = "[文件最大上传大小限制]默认为5Mb",example = "5")
    private int fileMaxSize;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序",example = "1")
    private int sort;


    /**
     * 更新为null时设置值
     *
     */
    @SuppressWarnings("rawtypes")
    Class<? extends FieldBaseSupplier> updateWhenNullSet;

    /**
     * 插入为null时设置值
     *
     */
    @SuppressWarnings("rawtypes")
    Class<? extends FieldBaseSupplier> insertWhenNullSet;

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
