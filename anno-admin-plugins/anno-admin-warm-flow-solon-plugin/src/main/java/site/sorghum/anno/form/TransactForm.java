package site.sorghum.anno.form;

import lombok.Data;
import site.sorghum.anno.anno.annotation.clazz.AnnoForm;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionType;
import site.sorghum.anno.anno.enums.AnnoDataType;
import site.sorghum.anno.anno.form.BaseForm;

@AnnoForm(
    name = "办理"
)
@Data
public class TransactForm implements BaseForm {
    @AnnoField(
        title = "审批建议",
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.TEXT_AREA
    )
    String message;

    @AnnoField(
        title = "审批状态",
        edit = @AnnoEdit(
            editEnable = true,
            addEnable = true
        ),
        dataType = AnnoDataType.OPTIONS,
        optionType = @AnnoOptionType(
            value = {
                @AnnoOptionType.OptionData(
                    label = "通过",
                    value = "1"
                ),
                @AnnoOptionType.OptionData(
                    label = "驳回",
                    value = "2"
                )
            }
        )
    )
    Integer status;
}
