package site.sorghum.anno.plugin.form;


import jakarta.inject.Named;
import lombok.Data;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.annotation.clazz.AnnoForm;
import site.sorghum.anno.anno.annotation.field.AnnoEdit;
import site.sorghum.anno.anno.annotation.field.AnnoField;
import site.sorghum.anno.anno.annotation.field.AnnoSearch;
import site.sorghum.anno.anno.form.BaseForm;

@Data
@Named
@AnnoForm(name = "重置密码")
public class ResetPwdForm implements BaseForm {

    @AnnoField(title = "新密码",edit = @AnnoEdit(span = 24),search = @AnnoSearch)
    String newPwd1;

    @AnnoField(title = "确认新密码",edit = @AnnoEdit(span = 24),search = @AnnoSearch)
    String newPwd2;

}
