package site.sorghum.anno.anno.proxy;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.hutool.core.util.StrUtil;
import org.noear.solon.annotation.Component;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno.anno.interfaces.CheckPermissionFunction;

/**
 * 许可代理
 *
 * @author Sorghum
 * @since 2023/06/05
 */
@Component
@org.springframework.stereotype.Component
public class PermissionProxy {

    public static final String VIEW = "view";

    public static final String VIEW_TRANSLATE = "查看";

    public static final String ADD = "add";

    public static final String ADD_TRANSLATE = "增加";

    public static final String UPDATE = "update";

    public static final String UPDATE_TRANSLATE = "修改";

    public static final String DELETE = "delete";

    public static final String DELETE_TRANSLATE = "删除";

    public void checkPermission(AnEntity anEntity, String code) {
        if (isSystemRun() || StrUtil.isBlank(code)) {
            return;
        }
        // 校验登录
        CheckPermissionFunction.loginCheckFunction.run();
        boolean enable = anEntity.isEnablePermission();
        if (!enable) {
            return;
        }
        String baseCode = anEntity.getPermissionCode();
        // 校验权限
        CheckPermissionFunction.permissionCheckFunction.accept(baseCode + ":" + code);
    }

    private boolean isSystemRun() {
        SaRequest request = SaHolder.getRequest();
        return request == null || request.getSource() == null;
    }
}
