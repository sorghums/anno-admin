package site.sorghum.anno.test;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.annotation.SolonMain;
import org.noear.solon.web.cors.CrossHandler;
import org.noear.wood.WoodConfig;
import site.sorghum.anno.anno.annotation.global.AnnoScan;

/**
 * Ano 管理入门
 *
 * @author Sorghum
 * @since 2023/06/05
 */
@SolonMain
@Slf4j
@AnnoScan(scanPackage = "site.sorghum.anno")
public class AnnoAdminStarter {
    public static void main(String[] args) {
        SolonApp start = Solon.start(AnnoAdminStarter.class, args, app -> {
            //执行后打印sql
            WoodConfig.onExecuteAft(cmd -> {
                log.debug("===[Wood] sql: {}", cmd.text);
                log.debug("===[Wood] var: {}", cmd.paramMap());
            });
            app.before(new CrossHandler().allowedOrigins("*"));
        });
    }
}
