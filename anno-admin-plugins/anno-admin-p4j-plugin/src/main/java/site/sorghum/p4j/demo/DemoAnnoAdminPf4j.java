package site.sorghum.p4j.demo;

import org.pf4j.Extension;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.MetadataLoader;
import site.sorghum.anno.auth.AnnoAuthUser;
import site.sorghum.p4j.AnnoAdminPf4j;

@Extension
public class DemoAnnoAdminPf4j implements AnnoAdminPf4j {
    @Override
    public void loadEntity() {
        MetadataLoader metadataLoader = AnnoBeanUtils.getBean(MetadataLoader.class);
        metadataLoader.load(AnnoAuthUser.class);
        System.out.println("DemoAnnoAdminPf4j loaded!!!");
    }
}
