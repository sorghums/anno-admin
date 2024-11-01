<#-- Import statements -->
import lombok.Data;
import site.sorghum.anno.anno.annotation.clazz.*;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.annotation.field.type.*;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.option.OptionDataSupplier;
import site.sorghum.anno.anno.proxy.field.EmptyFieldBaseSupplier;
import site.sorghum.anno.anno.proxy.field.SnowIdSupplier;
import site.sorghum.plugin.join.aop.JoinResMap;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

<#-- Class definition -->
<#-- Use @Data annotation from Lombok -->
@Data
@AnnoMain(
    name = "${main.name}",
    tableName = "${main.tableName}",
    <#if main.annoOrder??>
        annoOrder = {
        <#list main.annoOrder as order>
            @AnnoOrder(orderValue = "${order.orderValue}", orderType = "${order.orderType}"),
        </#list>
        },
    </#if>
    annoPermission = @AnnoPermission(baseCode = "${main.tableName}", baseCodeTranslate = "${main.name}"),
    canRemove = ${main.canRemove?c},
    autoMaintainTable = ${main.autoMaintainTable?c}
)
public class ${main.className} {

<#-- Fields definition -->
<#list main.fields as field>
    @AnnoField(
        tableFieldName = "${field.tableFieldName}",
        fieldSize = ${field.fieldSize},
        show = ${field.show?c},
        pkField = ${field.pkField?c},
        <#if field.edit??>
            edit = @AnnoEdit(
            editEnable = ${field.edit.editEnable?c},
            addEnable = ${field.edit.addEnable?c}
            ),
        </#if>
        <#if field.search??>
            search = @AnnoSearch(
            notNull = ${field.search.notNull?c},
            defaultValue = "${field.search.defaultValue}"
        ),
        </#if>
        dataType = AnnoDataType.${field.dataType?string},
        <#if field.optionType??>
            optionType = @AnnoOptionType(
            <#if field.optionType.value??>
                value = {
                <#list field.optionType.value as option>
                    @AnnoOptionType.OptionData(value = "${option.value}", label = "${option.label}"),
                </#list>
                }
            </#if>
            <#if field.optionType.optionAnnoClass??>
                optionAnno = @AnnoOptionType.OptionAnnoClass(annoClass = ${field.optionType.optionAnnoClass}.class)
            </#if>
            ),
        </#if>
        <#if field.imageType??>
                imageType = @AnnoImageType(
                enlargeAble = ${field.imageType.enlargeAble?c},
                width = ${field.imageType.width},
                height = ${field.imageType.height}
            ),
        </#if>
        <#if field.codeType??>
            codeType = @AnnoCodeType(
                mode = "${field.codeType.mode}"
            ),
        </#if>
        <#if field.treeType??>
            treeType = @AnnoTreeType(
            <#if field.treeType.value??>
                value = {
                <#list field.treeType.value as treeData>
                    @AnnoTreeType.TreeData(id = "${treeData.id}", label = "${treeData.label}", pid = "${treeData.pid}"),
                </#list>
                }
            </#if>
            <#if field.treeType.treeAnnoClass??>
                treeAnno = @AnnoTreeType.TreeAnnoClass(annoClass = ${field.treeType.treeAnnoClass}.class)
            </#if>
            ),
        </#if>
        <#if field.fileType??>
                fileType = @AnnoFileType(
                fileType = "${field.fileType.fileType}",
                fileMaxCount = ${field.fileType.fileMaxCount},
                fileMaxSize = ${field.fileType.fileMaxSize}
            ),
        </#if>
        title = "${field.title}"
    )
    ${field.type} ${field.name};

</#list>

    @JoinResMap
    Map<String, Object> joinResMap = new HashMap<>();
}