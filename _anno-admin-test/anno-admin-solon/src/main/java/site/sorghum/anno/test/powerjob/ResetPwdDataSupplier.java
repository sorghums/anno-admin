package site.sorghum.anno.test.powerjob;

import cn.hutool.core.util.RandomUtil;
import org.noear.solon.annotation.Component;
import site.sorghum.anno.anno.datasupplier.AnnoDynamicFormAndDataSupplier;
import site.sorghum.anno.plugin.form.ResetPwdForm;

import java.util.List;
import java.util.Map;

@Component
public class ResetPwdDataSupplier implements AnnoDynamicFormAndDataSupplier {

    public ResetPwdDataSupplier() {
        AnnoDynamicFormAndDataSupplier.putInstance(ResetPwdForm.class, this);
    }

    @Override
    public Map<String, Object> get(String entityName, List<String> columnDataIds) {
        String newPwd = RandomUtil.randomString(8);
        return Map.of(
            "newPwd1", newPwd,
            "newPwd2", newPwd
        );
    }
}
