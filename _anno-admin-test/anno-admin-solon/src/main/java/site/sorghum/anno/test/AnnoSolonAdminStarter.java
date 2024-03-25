package site.sorghum.anno.test;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;
import org.noear.solon.scheduling.annotation.EnableRetry;
import org.noear.solon.scheduling.annotation.EnableScheduling;
import site.sorghum.anno.anno.annotation.global.AnnoScan;
import site.sorghum.anno.anno.interfaces.CheckPermissionFunction;
import site.sorghum.anno.anno.proxy.PermissionProxy;

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
    public static void main(String[] args) {
        Solon.start(AnnoSolonAdminStarter.class, args);
        CheckPermissionFunction.loginCheckFunction = () -> {
            System.out.println("校验权限！");
        };
        CheckPermissionFunction.permissionCheckFunction = (permissionCode) -> {
            System.out.println("校验权限！");
        };
    }
}
