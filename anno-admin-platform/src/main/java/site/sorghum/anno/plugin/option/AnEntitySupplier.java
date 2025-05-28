package site.sorghum.anno.plugin.option;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionTypeImpl;
import site.sorghum.anno.anno.option.OptionDataSupplier;

import java.util.List;

@Named
public class AnEntitySupplier implements OptionDataSupplier {

    @Inject
    MetadataManager metadataManager;

    public AnEntitySupplier() {
        init();
    }

    @Override
    public List<AnnoOptionTypeImpl.OptionDataImpl> getOptionDataList() {
        return metadataManager.getAllEntity().stream().map(
            it-> new AnnoOptionTypeImpl.OptionDataImpl(it.getName(), it.getEntityName())
        ).toList();
    }
}
