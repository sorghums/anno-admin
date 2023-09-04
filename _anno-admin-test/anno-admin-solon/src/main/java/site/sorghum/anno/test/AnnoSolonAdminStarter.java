package site.sorghum.anno.test;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.annotation.SolonMain;
import org.noear.solon.scheduling.annotation.EnableScheduling;
import org.noear.solon.web.cors.CrossHandler;
import org.noear.wood.WoodConfig;
import site.sorghum.anno.anno.annotation.global.AnnoScan;
import site.sorghum.anno.solon.interceptor.WoodSqlLogInterceptor;

/**
 * Ano 管理入门
 *
 * @author Sorghum
 * @since 2023/06/05
 */
@SolonMain
@Slf4j
@EnableScheduling
@AnnoScan(scanPackage = {"site.sorghum.anno", "tech.powerjob.server.solon"})
public class AnnoSolonAdminStarter {
    public static void main(String[] args) {
        SolonApp start = Solon.start(AnnoSolonAdminStarter.class, args, app -> {
            //执行后打印sql
            WoodConfig.onExecuteAft(new WoodSqlLogInterceptor());
            app.before(new CrossHandler().allowedOrigins("*").allowedHeaders("*").allowedMethods("*"));
        });
    }
}
