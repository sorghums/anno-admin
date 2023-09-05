package site.sorghum.anno.anno.interfaces;

import cn.dev33.satoken.stp.StpUtil;

import java.util.function.Consumer;

/**
 * 检查函数
 *
 * @author sorghum
 * @date 2023/09/05
 */
public class CheckPermissionFunction {
    /**
     * 权限检查函数
     */
    public static Consumer<String> permissionCheckFunction = StpUtil::checkPermission;

    /**
     * 登录检查函数
     */
    public static Runnable loginCheckFunction = StpUtil::checkLogin;
}
