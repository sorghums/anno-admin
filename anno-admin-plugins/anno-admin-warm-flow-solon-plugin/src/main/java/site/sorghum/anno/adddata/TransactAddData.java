package site.sorghum.anno.adddata;

import org.noear.solon.annotation.Component;
import site.sorghum.anno.anno.datasupplier.AnnoAddDataSupplier;
import site.sorghum.anno.form.TransactForm;

import java.util.List;
import java.util.Map;

@Component
public class TransactAddData implements AnnoAddDataSupplier {

    public TransactAddData() {
        AnnoAddDataSupplier.putInstance(TransactForm.class, this);
    }

    @Override
    public Map<String, Object> get(String entityName, List<String> columnDataIds) {
        return Map.of(
            "status","1",
            "message","审核默认通过"
        );
    }
}
