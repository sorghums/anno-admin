package site.sorghum.anno.anno.proxy;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Named;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno.anno.annotation.clazz.AnnoPermissionImpl;
import site.sorghum.anno.anno.functions.AnnoFunction;

/**
 * 许可代理
 *
 * @author Sorghum
 * @since 2023/06/05
 */
@Named
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
        if (isSystemRun()) {
            return;
        }
        // 校验登录
        checkLogin();
        AnnoPermissionImpl annoPermission = anEntity.getAnnoPermission();
        boolean enable = annoPermission.enable();
        if (!enable) {
            return;
        }
        String baseCode = annoPermission.baseCode();
        // 校验权限
        String permissionCode = StrUtil.isNotBlank(code) ? baseCode + ":" + code : baseCode;
        AnnoFunction.permissionCheckFunction.accept(permissionCode);
    }

    public void checkLogin() {
        if (isSystemRun()) {
            return;
        }
        AnnoFunction.loginCheckFunction.run();
    }

    private boolean isSystemRun() {
        SaRequest request = SaHolder.getRequest();
        return request == null || request.getSource() == null;
    }


}
