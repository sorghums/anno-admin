package site.sorghum.anno._metadata;


import lombok.AllArgsConstructor;
import lombok.Data;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoImageType;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeType;
import site.sorghum.anno.anno.enums.AnnoDataType;

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
    private String fieldName;

    /**
     * 标题
     *
     * @see AnnoField#title()
     */
    private String title;

    /**
     * 表字段名
     *
     * @see AnnoField#tableFieldName()
     */
    private String tableFieldName;

    /**
     * 是否主键
     */
    private boolean primaryKey;

    /**
     * 字段类型
     */
    private Class<?> fieldType;

    /**
     * 数据库中的字段长度，为 0 时使用 anno 设置的默认长度
     *
     * @see AnnoField#fieldSize()
     */
    private int fieldSize;

    /**
     * 定义数据库中的默认值，比如：DEFAULT 0
     *
     * @see AnnoField#defaultValue()
     */
    private String defaultValue;

    /**
     * 显示
     *
     * @see AnnoField#show()
     */
    private boolean show;

    /**
     * 搜索 是否开启
     *
     * @see AnnoSearch#enable()
     */
    private boolean searchEnable;

    /**
     * 搜索 是否必填
     *
     * @see AnnoSearch#notNull()
     */
    private boolean searchNotNull;

    /**
     * 搜索 提示信息
     *
     * @see AnnoSearch#placeHolder()
     */
    private String searchPlaceHolder;

    /**
     * 编辑 是否开启
     *
     * @see AnnoEdit#editEnable()
     */
    private boolean editEnable;

    /**
     * 编辑 是否必填
     *
     * @see AnnoEdit#notNull()
     */
    private boolean editNotNull;

    /**
     * 编辑 提示信息
     *
     * @see AnnoEdit#placeHolder()
     */
    private String editPlaceHolder;

    /**
     * 编辑 新增启用
     *
     * @see AnnoEdit#addEnable()
     */
    private boolean addEnable;

    /**
     * 数据类型
     *
     * @see AnnoField#dataType()
     */
    private AnnoDataType dataType;

    /**
     * 数据类型
     *
     * @see AnnoOptionType#sql()
     */
    private String optionTypeSql;

    /**
     * 选择数据
     *
     * @see AnnoOptionType.OptionData
     */
    private List<OptionData> optionDatas;

    @Data
    @AllArgsConstructor
    public static class OptionData {

        /**
         * 页面显示的内容
         *
         * @see AnnoOptionType.OptionData#label()
         */
        private String label;

        /**
         * 传递给后端的值
         *
         * @see AnnoOptionType.OptionData#value()
         */
        private String value;
    }

    /**
     * 图像 预览图比率
     *
     * @see AnnoImageType#thumbRatio()
     */
    private AnnoImageType.ThumbRatio imageThumbRatio;

    /**
     * 图像 图片模式
     *
     * @see AnnoImageType#thumbMode()
     */
    private AnnoImageType.ThumbMode imageThumbMode;

    /**
     * 图像 点击可放大展示
     *
     * @see AnnoImageType#enlargeAble()
     */
    private boolean imageEnlargeAble;

    /**
     * 图像 宽度 px
     *
     * @see AnnoImageType#width()
     */
    private int imageWidth;

    /**
     * 图像 高度 px
     *
     * @see AnnoImageType#height()
     */
    private int imageHeight;

    /**
     * 选择类型-树形
     *
     * @see AnnoTreeType#sql()
     */
    private String treeTypeSql;

    /**
     * 选择类型-树形
     *
     * @see AnnoTreeType#value()
     */
    private List<TreeData> treeDatas;

    @Data
    @AllArgsConstructor
    public static class TreeData {

        /**
         * 节点id
         *
         * @see AnnoTreeType.TreeData#id()
         */
        private String id;

        /**
         * 页面显示的内容
         *
         * @see AnnoTreeType.TreeData#label()
         */
        private String label;

        /**
         * 传递给后端的值
         *
         * @see AnnoTreeType.TreeData#value()
         */
        private String value;

        /**
         * 父节点id
         *
         * @see AnnoTreeType.TreeData#pid()
         */
        private String pid;
    }


}
