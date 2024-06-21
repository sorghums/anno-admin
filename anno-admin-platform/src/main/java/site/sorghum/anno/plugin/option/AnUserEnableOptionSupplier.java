package site.sorghum.anno.plugin.option;

import jakarta.inject.Named;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno.anno.option.OptionDataSupplier;

import java.util.List;

@Named
public class AnUserEnableOptionSupplier implements OptionDataSupplier {

    public AnUserEnableOptionSupplier() {
        init();
    }

    @Override
    public List<AnField.OptionData> getOptionDataList() {
        return List.of(
            new AnField.OptionData("正常", "1"),
            new AnField.OptionData("封禁", "0")
        );
    }
}
