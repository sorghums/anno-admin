package site.sorghum.anno;


import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno.db.service.DbService;
import site.sorghum.anno.db.service.impl.DbServiceWood;
import site.sorghum.anno.modular.ddl.InitDataService;
import site.sorghum.anno.modular.menu.entity.anno.SysAnnoMenu;
import site.sorghum.anno.modular.model.AnnoModule;

/**
 * 基础模块
 *
 * @author sorghum
 * @date 2023/07/15
 */
@Slf4j
@Component
public class BaseAnnoModule extends AnnoModule {
    @Inject("dbServiceWood")
    DbService dbServiceWood;

    public BaseAnnoModule() {
        super("[Anno]基础模块", "包含B端用户，角色，组织，权限等。");
    }
    public BaseAnnoModule(String modelName, String modelDesc) {
        super(modelName, modelDesc);
    }

    @Override
    @Init
    public void run() {
        super.run();
    }
}
