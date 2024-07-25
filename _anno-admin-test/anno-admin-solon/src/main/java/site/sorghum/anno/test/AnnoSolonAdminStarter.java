package site.sorghum.anno.test;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;
import org.noear.solon.scheduling.annotation.EnableRetry;
import org.noear.solon.scheduling.annotation.EnableScheduling;
import site.sorghum.anno.anno.annotation.global.AnnoScan;
import site.sorghum.anno.pf4j.Pf4jRunner;

import java.nio.file.Path;

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
@AnnoScan(scanPackage = {"tech.powerjob.server.solon"})
public class AnnoSolonAdminStarter {
    public static void main(String[] args) throws Exception {
        Solon.start(AnnoSolonAdminStarter.class, args);
        String pluginId = Pf4jRunner.runPlugin(Path.of("D:\\Project\\rep\\anno-admin-demo-p4j-plugin\\target\\anno-admin-demo-p4j-plugin-1.0-SNAPSHOT.jar"));
        System.out.println("");
    }
}
