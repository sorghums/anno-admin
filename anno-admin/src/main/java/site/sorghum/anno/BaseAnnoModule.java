package site.sorghum.anno;


import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno.modular.model.AnnoModule;

/**
 * 基础模块
 *
 * @author sorghum
 * @since 2023/07/15
 */
@Slf4j
@Named
public class BaseAnnoModule extends AnnoModule {

    public BaseAnnoModule() {
        super("基础模块", "包含B端用户，角色，组织，权限等。");
    }

    @Override
    public void run() {
        super.run();
    }
}
