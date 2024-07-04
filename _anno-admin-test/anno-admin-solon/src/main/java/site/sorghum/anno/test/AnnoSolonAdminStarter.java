package site.sorghum.anno.test;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.type.TypeDescription;
import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;
import org.noear.solon.scheduling.annotation.EnableRetry;
import org.noear.solon.scheduling.annotation.EnableScheduling;
import site.sorghum.anno.DemoUtil;
import site.sorghum.anno._metadata.MetadataLoader;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.annotation.global.AnnoScan;

import java.util.Map;

/**
 * Ano 管理入门
 *
 * @author Sorghum
 * @since 2023/06/05
 */
@SolonMain
@Slf4j
@EnableRetry
@EnableScheduling
@AnnoScan(scanPackage = {"site.sorghum.anno", "tech.powerjob.server.solon"})
public class AnnoSolonAdminStarter {
    public static void main(String[] args) throws Exception {
        Solon.start(AnnoSolonAdminStarter.class, args);
        MetadataManager bean = Solon.context().getBean(MetadataManager.class);
        Map<TypeDescription, Class<?>> typeDescriptionClassMap = DemoUtil.loadClass();
        for (Class<?>  value: typeDescriptionClassMap.values()) {
            bean.loadEntity(value);
        }
        bean.refresh();
    }

}
