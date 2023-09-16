package site.sorghum.anno;


import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno.anno.interfaces.CheckPermissionFunction;
import site.sorghum.anno.plugin.AnnoPlugin;
import site.sorghum.anno.plugin.service.AuthService;

/**
 * 基础模块
 *
 * @author sorghum
 * @since 2023/07/15
 */
@Slf4j
@Named
public class BaseAnnoPlugin extends AnnoPlugin {

    public BaseAnnoPlugin() {
        super("管理端插件", "包含B端用户，角色，组织，权限等。");
    }

    @Override
    public int runOrder() {
        return 100;
    }

    @Override
    public void run() {
        CheckPermissionFunction.permissionCheckFunction = (permissionCode) -> {
            AuthService authService = AnnoBeanUtils.getBean(AuthService.class);
            authService.verifyPermission(permissionCode);
        };
    }
}
