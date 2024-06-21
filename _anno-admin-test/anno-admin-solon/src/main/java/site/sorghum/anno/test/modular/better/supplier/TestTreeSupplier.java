package site.sorghum.anno.test.modular.better.supplier;

import org.noear.solon.annotation.Component;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno.anno.tree.TreeDataSupplier;

import java.util.List;

@Component
public class TestTreeSupplier implements TreeDataSupplier {
    public TestTreeSupplier() {
        init();
    }

    @Override
    public List<AnField.TreeData> getTreeDataList() {
        return List.of(
            new AnField.TreeData("1", "一",null),
            new AnField.TreeData("2", "二","1"),
            new AnField.TreeData("3", "三",null)
        );
    }
}
