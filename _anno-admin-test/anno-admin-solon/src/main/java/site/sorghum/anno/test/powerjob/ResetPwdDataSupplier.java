package site.sorghum.anno.test.powerjob;

import org.noear.solon.annotation.Component;
import site.sorghum.anno.anno.datasupplier.AnnoAddDataSupplier;
import site.sorghum.anno.plugin.form.ResetPwdForm;

import java.util.List;
import java.util.Map;

@Component
public class ResetPwdDataSupplier implements AnnoAddDataSupplier {

    public ResetPwdDataSupplier() {
        AnnoAddDataSupplier.putInstance(ResetPwdForm.class, this);
    }

    @Override
    public Map<String, Object> get(String entityName, List<String> columnDataIds) {
        return Map.of(
            "newPwd1","123456"
        );
    }
}
