package site.sorghum.anno.test;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.annotation.SolonMain;
import org.noear.wood.IPage;
import org.noear.wood.WoodConfig;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno.db.param.PageParam;
import site.sorghum.anno.db.param.RemoveParam;
import site.sorghum.anno.db.param.TableParam;
import site.sorghum.anno.modular.anno.annotation.global.AnnoScan;
import site.sorghum.anno.modular.system.anno.SysRole;

import java.util.ArrayList;

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
        });
        DbService dbService = start.context().getBean(DbService.class);
        IPage<SysRole> page = dbService.page(
                new TableParam<SysRole>() {{
                    setClazz(SysRole.class);
                    setTableName("sys_role");
                    setRemoveParam(new RemoveParam(){{
                        setLogic(true);
                    }});
                }}, new ArrayList<>(), new PageParam(1, 10)
        );
        System.out.println(page.getList());
    }
}
