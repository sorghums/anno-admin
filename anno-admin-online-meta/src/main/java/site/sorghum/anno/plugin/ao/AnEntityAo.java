package site.sorghum.anno.plugin.ao;

import org.noear.wood.annotation.PrimaryKey;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.proxy.field.SnowIdSupplier;
import site.sorghum.anno.db.QueryType;
import site.sorghum.plugin.join.aop.JoinResMap;

import java.util.HashMap;
import java.util.Map;

/**
 * 主要MainAo
 *
 * @author Sorghum
 * @since 2024/02/26
 */
@AnnoMain(name = "元数据管理", tableName = "an_entity_ao")
public class AnEntityAo extends AnEntity {

    @AnnoField(title = "主键", tableFieldName = "id", show = false, fieldSize = 32, insertWhenNullSet = SnowIdSupplier.class)
    @PrimaryKey
    String id;

    @Override
    @AnnoField(
        title = "实体名称",
        tableFieldName = "entity_name",
        search = @AnnoSearch(queryType = QueryType.LIKE),
        edit = @AnnoEdit(placeHolder = "请输入实体名称"))
    public String getTitle() {
        return super.getTitle();
    }

    @AnnoField(title = "虚拟表", tableFieldName = "virtual_table", edit = @AnnoEdit,
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(value = {
            @AnnoOptionType.OptionData(label = "是", value = "1"),
            @AnnoOptionType.OptionData(label = "否", value = "0"),
        }))
    public Integer virtualTableField;

    @Override
    @AnnoField(
        title = "请输入实体数据表名称",
        tableFieldName = "entity_table_name",
        search = @AnnoSearch(queryType = QueryType.LIKE),
        edit = @AnnoEdit(placeHolder = "请输入实体数据表名称",showBy = @AnnoEdit.ShowBy(expr = "annoDataForm.virtualTable == 1")))
    public String getTableName() {
        return super.getTableName();
    }

    @Override
    public boolean isOrgFilter() {
        return super.isOrgFilter();
    }

    @Override
    public boolean isCanRemove() {
        return super.isCanRemove();
    }



    @Override
    public boolean isAutoMaintainTable() {
        return super.isAutoMaintainTable();
    }

    @Override
    public Class<?> getClazz() {
        return super.getClazz();
    }



    @JoinResMap
    Map<String,Object> joinResMap = new HashMap<>();
}
