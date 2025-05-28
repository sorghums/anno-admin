package site.sorghum.anno.plugin.ao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.clazz.AnnoOrder;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermission;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.type.AnnoCodeType;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.db.BaseMetaModel;
import site.sorghum.anno.plugin.option.AnEntityFieldSupplier;
import site.sorghum.anno.plugin.option.AnEntitySupplier;

import java.io.Serializable;

@AnnoMain(
    name = "渲染代码",
    tableName = "an_render",
    annoPermission = @AnnoPermission(
        baseCode = "an_render",
        baseCodeTranslate = "渲染代码"
    ),
    annoOrder = {
        @AnnoOrder(orderValue = "id", orderType = "desc")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
public class AnRender extends BaseMetaModel implements Serializable {
    @AnnoField(
        title = "实体类",
        tableFieldName = "entity",
        edit = @AnnoEdit,
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(
            supplier = AnEntitySupplier.class
        )
    )
    String entity;

    @AnnoField(
        title = "字段",
        tableFieldName = "field",
        edit = @AnnoEdit,
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(
            supplier = AnEntityFieldSupplier.class
        )
    )
    String field;

    @AnnoField(title = "渲染代码",
        tableFieldName = "render_code",
        edit = @AnnoEdit,
        dataType = AnnoDataType.CODE_EDITOR,
        codeType = @AnnoCodeType(mode = "text/x-html"))
    String renderCode;

}
