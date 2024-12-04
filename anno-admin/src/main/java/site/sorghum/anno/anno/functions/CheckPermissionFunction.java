package site.sorghum.anno.anno.functions;


import site.sorghum.anno.auth.AnnoStpUtil;

import java.util.function.Consumer;

/**
 * 检查函数
 *
 * @author sorghum
 * @since 2023/09/05
 */
public class CheckPermissionFunction {
    /**
     * 权限检查函数
     */
    public static Consumer<String> permissionCheckFunction = AnnoStpUtil::checkPermission;

    /**
     * 登录检查函数
     */
    public static Runnable loginCheckFunction = AnnoStpUtil::checkLogin;
}
