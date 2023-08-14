package site.sorghum.anno._metadata;

import lombok.Data;
import org.noear.wood.annotation.Table;
import site.sorghum.anno.anno.annotation.clazz.*;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.anno.proxy.AnnoPreBaseProxy;

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
    private String title;

    /**
     * 表名
     *
     * @see Table#value()
     */
    private String tableName;

    /**
     * 是否需要组织过滤
     */
    boolean orgFilter;

    /**
     * 是否虚拟表
     *
     * @see AnnoMain#virtualTable()
     */
    private boolean virtualTable = false;

    /**
     * 是否自动维护表结构
     */
    private boolean isAutoMaintainTable = true;

    /**
     * 对应的类
     */
    private Class<?> clazz;

    /**
     * 一般是类名
     */
    private String entityName;

    /**
     * 排序类型 asc, desc
     *
     * @see AnnoOrder#orderType()
     */
    private String orderType;

    /**
     * 排序
     *
     * @see AnnoOrder#orderValue()
     */
    private String orderValue;

    /**
     * 前置代理类
     *
     * @see AnnoProxy#value()
     */
    private Class<? extends AnnoPreBaseProxy> preProxy;

    /**
     * 代理类
     *
     * @see AnnoProxy#value()
     */
    private Class<? extends AnnoBaseProxy> proxy;

    /**
     * 主键字段
     */
    private AnField pkField;

    private List<AnField> fields;

    private Map<String, AnField> fieldMap;

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
    private List<AnColumnButton> columnButtons;

    /**
     * 表级按钮
     */
    private List<AnTableButton> tableButtons;

    /**
     * 逻辑删除类型： 0 物理删除 1 逻辑删除
     *
     * @see AnnoRemove#removeType()
     */
    private int removeType = 0;

    /**
     * 逻辑删除值
     *
     * @see AnnoRemove#removeValue()
     */
    private String removeValue;

    /**
     * 数据未被删除时的标记
     *
     * @see AnnoRemove#notRemoveValue()
     */
    private String notRemoveValue;

    /**
     * 逻辑删除字段
     *
     * @see AnnoRemove#removeField()
     */
    private String removeField;

    /**
     * 是否启用权限
     *
     * @see AnnoPermission#enable()
     */
    private boolean enablePermission = false;

    /**
     * 权限码
     *
     * @see AnnoPermission#baseCode()
     */
    private String permissionCode;

    /**
     * 权限码翻译
     *
     * @see AnnoPermission#baseCodeTranslate()
     */
    private String permissionCodeTranslate;

    /**
     * 是否启用 左树
     *
     * @see AnnoLeftTree#enable()
     */
    private boolean enableLeftTree = false;

    /**
     * 左树分类键
     *
     * @see AnnoLeftTree#catKey()
     */
    private String leftTreeCatKey;

    /**
     * 左树类
     *
     * @see AnnoLeftTree#treeClass()
     */
    private Class<?> leftTreeClass;

    /**
     * 是否启用 Anno 树
     *
     * @see AnnoTree#enable()
     */
    private boolean enableTree = false;

    /**
     * Anno 树父键
     *
     * @see AnnoTree#parentKey()
     */
    private String treeParentKey;

    /**
     * Anno 树键
     *
     * @see AnnoTree#key()
     */
    private String treeKey;

    /**
     * Anno 树标签
     *
     * @see AnnoTree#label()
     */
    private String treeLabel;

    /**
     * Anno 树是否展示为树
     *
     * @see AnnoTree#displayAsTree()
     */
    private boolean treeDisplayAsTree = false;

}
