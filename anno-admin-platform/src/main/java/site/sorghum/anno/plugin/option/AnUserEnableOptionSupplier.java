package site.sorghum.anno.plugin.option;

import jakarta.inject.Named;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionTypeImpl;
import site.sorghum.anno.anno.option.OptionDataSupplier;

import java.util.List;

@Named
public class AnUserEnableOptionSupplier implements OptionDataSupplier {

    public AnUserEnableOptionSupplier() {
        init();
    }

    @Override
    public List<AnnoOptionTypeImpl.OptionDataImpl> getOptionDataList() {
        return List.of(
            new AnnoOptionTypeImpl.OptionDataImpl("正常", "1"),
            new AnnoOptionTypeImpl.OptionDataImpl("封禁", "0")
        );
    }
}
