package site.sorghum.anno.test.modular.better.supplier;

import org.noear.solon.annotation.Component;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno.anno.annotation.field.type.AnnoTreeTypeImpl;
import site.sorghum.anno.anno.tree.TreeDataSupplier;

import java.util.List;

@Component
public class TestTreeSupplier implements TreeDataSupplier {
    public TestTreeSupplier() {
        init();
    }

    @Override
    public List<AnnoTreeTypeImpl.TreeDataImpl> getTreeDataList() {
        return List.of(
            new AnnoTreeTypeImpl.TreeDataImpl("1", "一",null),
            new AnnoTreeTypeImpl.TreeDataImpl("2", "二","1"),
            new AnnoTreeTypeImpl.TreeDataImpl("3", "三",null)
        );
    }
}
