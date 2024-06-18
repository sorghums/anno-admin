package site.sorghum.anno.test;

import com.warm.flow.core.dto.FlowParams;
import com.warm.flow.core.enums.SkipType;
import com.warm.flow.core.service.DefService;
import com.warm.flow.core.service.InsService;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;
import org.noear.solon.scheduling.annotation.EnableRetry;
import org.noear.solon.scheduling.annotation.EnableScheduling;
import site.sorghum.anno.anno.annotation.global.AnnoScan;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

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
        DefService defService = Solon.context().getBean(DefService.class);
        InsService insService = Solon.context().getBean(InsService.class);
        String path = "D:\\Project\\rep\\opensource\\anno-admin\\anno-admin-warm-flow-solon-plugin\\src\\main\\resources\\demo\\leaveFlow-serial1.xml";
        defService.publish(1252655843128774656L);
        System.out.println("已开启的流程实例id：" + insService.start("1", getUser()).getId());

    }

    public static FlowParams getUser() {
        return FlowParams.build().flowCode("leaveFlow-serial1")
            .handler("1")
            .skipType(SkipType.PASS.getKey())
            .additionalHandler(Arrays.asList("role:100", "role:101"))
            .permissionFlag(Arrays.asList("role:1", "role:2"));
    }

}
