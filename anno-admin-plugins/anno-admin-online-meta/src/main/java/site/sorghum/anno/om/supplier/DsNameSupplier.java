package site.sorghum.anno.om.supplier;

import jakarta.inject.Named;
import org.noear.wood.DbContext;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionTypeImpl;
import site.sorghum.anno.anno.option.OptionDataSupplier;

import java.util.HashMap;
import java.util.List;

@Named
public class DsNameSupplier implements OptionDataSupplier {

    public static HashMap<String, DbContext> dbContexts = new HashMap<>();

    public DsNameSupplier() {
        init();
    }

    @Override
    public List<AnnoOptionTypeImpl.OptionDataImpl> getOptionDataList() {
        List<DbContext> dbContexts = AnnoBeanUtils.getBeansOfType(DbContext.class);
        return dbContexts.stream().map(
            it -> {
                AnnoOptionTypeImpl.OptionDataImpl optionData = new AnnoOptionTypeImpl.OptionDataImpl();
                String name = it.getMetaData().getDataSource().toString();
                DsNameSupplier.dbContexts.put(name, it);
                optionData.setLabel(name);
                optionData.setValue(name);
                return optionData;
            }
        ).toList();
    }
}
