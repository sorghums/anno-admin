package site.sorghum.anno._metadata;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.noear.snack.annotation.ONodeAttr;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.*;
import site.sorghum.anno.anno.annotation.field.AnnoMany2ManyField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 被{@link AnnoMain}标识的 entity
 *
 * @author songyinyin
 * @since 2023/7/9 19:16
 */
@Data
public class AnEntity {

    /**
     * 实体中文名称
     *
     * @see AnnoMain#name()
     */
    @ApiModelProperty(value = "实体中文名称",example = "用户管理")
    private String title;

    /**
     * 表名
     *
     * @see Table#value()
     */
    @ApiModelProperty(value = "表名",example = "an_user")
    private String tableName;

    /**
     * 是否需要组织过滤
     */
    @ApiModelProperty(value = "是否需要组织过滤",example = "true")
    boolean orgFilter;

    /**
     * 是否可以删除
     */
    @ApiModelProperty(value = "是否可以删除",example = "true")
    boolean canRemove;

    /**
     * 是否虚拟表
     *
     * @see AnnoMain#virtualTable()
     */
    @ApiModelProperty(value = "是否虚拟表",example = "false")
    private boolean virtualTable = false;

    /**
     * 是否自动维护表结构
     */
    @ApiModelProperty(value = "是否自动维护表结构",example = "true")
    private boolean isAutoMaintainTable = true;

    /**
     * 对应的类
     */
    @ApiModelProperty(value = "对应的类",example = "site.sorghum.anno.plugin.ao.AnUser")
    private Class<?> clazz;

    /**
     * 一般是类名
     */
    @ApiModelProperty(value = "一般是类名",example = "AnUser")
    private String entityName;

    /**
     * 排序类型 asc, desc
     *
     * @see AnnoOrder#orderType()
     */
    @ApiModelProperty(value = "排序类型 asc, desc",example = "asc")
    private String orderType;

    /**
     * 排序
     *
     * @see AnnoOrder#orderValue()
     */
    @ApiModelProperty(value = "排序",example = "id")
    private String orderValue;

    /**
     * 主键字段
     */
    @ApiModelProperty(value = "主键字段")
    private AnField pkField;

    /**
     * 实体类字段
     */
    @ApiModelProperty(value = "实体类字段")
    private List<AnField> fields;

    /**
     * 多对多按钮
     * @see AnnoMany2ManyField
     */
    @ApiModelProperty(hidden = true)
    @ONodeAttr(serialize = false)
    private List<AnMany2ManyField> many2ManyFields;

    /**
     * 实体类字段字典映射
     */
    @ApiModelProperty(hidden = true)
    @ONodeAttr(serialize = false)
    private Map<String, AnField> fieldMap;

    /**
     * 连表信息
     */
    @ApiModelProperty(hidden = true)
    @ONodeAttr(serialize = false)
    AnJoinTable joinTable = null;

    public void setFields(List<AnField> fields) {
        this.fields = fields;
        fieldMap = fields.stream().collect(Collectors.toMap(AnField::getFieldName, e -> e));
    }

    public void addField(AnField field) {
        fields.add(field);
        if (fieldMap == null) {
            fieldMap = new HashMap<>();
        }
        fieldMap.put(field.getFieldName(), field);
    }

    /**
     * 获取字段
     *
     * @param fieldName 字段名
     */
    public AnField getField(String fieldName) {
        if (fieldMap == null) {
            return null;
        }
        return fieldMap.get(fieldName);
    }

    /**
     * 获取数据库字段
     * [排除虚拟列字段]
     */
    public List<AnField> getDbAnFields() {
        return fields.stream().filter(anField -> !anField.isVirtualColumn()).collect(Collectors.toList());
    }

    /**
     * 行级按钮
     */
    @ApiModelProperty(value = "行级按钮")
    private List<AnColumnButton> columnButtons;

    /**
     * 表级按钮
     */
    @ApiModelProperty(value = "表级按钮")
    private List<AnButton> tableButtons;

    /**
     * 逻辑删除类型： 0 物理删除 1 逻辑删除
     *
     * @see AnnoRemove#removeType()
     */
    @ApiModelProperty(value = "逻辑删除类型： 0 物理删除 1 逻辑删除",example = "0")
    private int removeType = 0;

    /**
     * 逻辑删除值
     *
     * @see AnnoRemove#removeValue()
     */
    @ApiModelProperty(value = "逻辑删除值",example = "1")
    private String removeValue;

    /**
     * 数据未被删除时的标记
     *
     * @see AnnoRemove#notRemoveValue()
     */
    @ApiModelProperty(value = "逻辑未删除值",example = "0")
    private String notRemoveValue;

    /**
     * 逻辑删除字段
     *
     * @see AnnoRemove#removeField()
     */
    @ApiModelProperty(value = "逻辑删除字段",example = "is_delete")
    private String removeField;

    /**
     * 是否启用权限
     *
     * @see AnnoPermission#enable()
     */
    @ApiModelProperty(value = "是否启用权限",example = "true")
    private boolean enablePermission = false;

    /**
     * 权限码
     *
     * @see AnnoPermission#baseCode()
     */
    @ApiModelProperty(value = "权限码",example = "anno_mine_permission_user")
    private String permissionCode;

    /**
     * 权限码翻译
     *
     * @see AnnoPermission#baseCodeTranslate()
     */
    @ApiModelProperty(value = "权限码翻译",example = "用户管理")
    private String permissionCodeTranslate;

    /**
     * 是否启用 左树
     *
     * @see AnnoLeftTree#enable()
     */
    @ApiModelProperty(value = "是否启用 左树",example = "true")
    private boolean enableLeftTree = false;

    /**
     * 左树分类键
     *
     * @see AnnoLeftTree#catKey()
     */
    @ApiModelProperty(value = "左树分类键[映射到queryForm后的值]",example = "user")
    private String leftTreeCatKey;

    /**
     * 左树类
     *
     * @see AnnoLeftTree#treeClass()
     */
    @ApiModelProperty(value = "左树类",example = "site.sorghum.anno.plugin.ao.AnUser")
    private Class<?> leftTreeClass;

    /**
     * 是否启用 Anno 树
     *
     * @see AnnoTree#enable()
     */
    @ApiModelProperty(value = "是否启用 Anno 树",example = "true")
    private boolean enableTree = false;

    /**
     * Anno 树父键
     *
     * @see AnnoTree#parentKey()
     */
    @ApiModelProperty(value = "Anno 树父键",example = "parent_id")
    private String treeParentKey;

    /**
     * Anno 树键
     *
     * @see AnnoTree#key()
     */
    @ApiModelProperty(value = "Anno 树键",example = "id")
    private String treeKey;

    /**
     * Anno 树标签
     *
     * @see AnnoTree#label()
     */
    @ApiModelProperty(value = "Anno 树标签",example = "name")
    private String treeLabel;

    /**
     * Anno 树是否展示为树
     *
     * @see AnnoTree#displayAsTree()
     */
    @ApiModelProperty(value = "Anno 树是否展示为树",example = "false")
    private boolean treeDisplayAsTree = false;

}
