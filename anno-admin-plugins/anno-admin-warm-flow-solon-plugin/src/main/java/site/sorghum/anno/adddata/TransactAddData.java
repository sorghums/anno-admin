package site.sorghum.anno.adddata;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno.anno.datasupplier.AnnoAddDataSupplier;
import site.sorghum.anno.ao.WaitFlowTaskAo;
import site.sorghum.anno.form.TransactForm;
import site.sorghum.anno.service.AnnoFlowService;

import java.util.List;
import java.util.Map;

@Component
public class TransactAddData implements AnnoAddDataSupplier {
    @Inject
    AnnoFlowService annoFlowService;

    public TransactAddData() {
        AnnoAddDataSupplier.putInstance(TransactForm.class, this);
    }

    @Override
    public Map<String, Object> get(String entityName, List<String> columnDataIds) {
        String id = columnDataIds.get(0);
        WaitFlowTaskAo waitFlowTaskAo =  annoFlowService.toDoOne(id);
        return Map.of(
            "status","1",
            "message","审核默认通过"
        );
    }
}
