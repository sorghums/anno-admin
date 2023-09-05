package site.sorghum.anno.test;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.annotation.SolonMain;
import org.noear.solon.web.cors.CrossHandler;
import org.noear.wood.WoodConfig;
import site.sorghum.anno.anno.annotation.global.AnnoScan;
import site.sorghum.anno.anno.interfaces.CheckPermissionFunction;

/**
 * Ano 管理入门
 *
 * @author Sorghum
 * @since 2023/06/05
 */
@SolonMain
@Slf4j
@AnnoScan(scanPackage = "site.sorghum.anno")
public class AnnoSolonAdminStarter {
    public static void main(String[] args) {
        SolonApp start = Solon.start(AnnoSolonAdminStarter.class, args, app -> {
            //执行后打印sql
            WoodConfig.onExecuteAft(cmd -> {
                log.debug("===[Wood] sql: {}", cmd.text);
                log.debug("===[Wood] var: {}", cmd.paramMap());
            });
            app.before(new CrossHandler().allowedOrigins("*").allowedHeaders("*").allowedMethods("*"));
        });
        // 忽略登录检查 (仅测试用)
        CheckPermissionFunction.loginCheckFunction = () -> {
            // StpUtil.checkLogin();
        };
        // 忽略权限检查 (仅测试用)
        CheckPermissionFunction.permissionCheckFunction = (code) -> {
            // log.info("===[Anno] permission check: {}", code);
            // StpUtil.checkPermission(code);
        };
    }
}
