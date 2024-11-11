package site.sorghum.anno.plugin.option;

import jakarta.inject.Named;
import site.sorghum.anno.anno.annotation.field.type.AnnoOptionTypeImpl;
import site.sorghum.anno.anno.entity.common.TagEnumLabel;
import site.sorghum.anno.anno.option.OptionDataSupplier;

import java.util.List;

@Named
public class SimpleEnableOptionSupplier implements OptionDataSupplier {

    public SimpleEnableOptionSupplier() {
        init();
    }

    @Override
    public List<AnnoOptionTypeImpl.OptionDataImpl> getOptionDataList() {
        return List.of(
            new AnnoOptionTypeImpl.OptionDataImpl(new TagEnumLabel("正常","green"), "1"),
            new AnnoOptionTypeImpl.OptionDataImpl(new TagEnumLabel("封禁","red"), "0")
        );
    }
}
