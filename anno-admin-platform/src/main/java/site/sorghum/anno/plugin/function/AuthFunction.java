package site.sorghum.anno.plugin.function;


import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno.plugin.ao.AnUser;
import site.sorghum.anno.plugin.entity.request.LoginReq;
import site.sorghum.anno.plugin.service.AuthService;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 认证功能函数接口
 * 提供认证相关的函数式操作方法
 */
public final class AuthFunction {

    /**
     * 验证登录凭证
     * 参数: LoginReq 登录请求对象
     * 返回: AnUser 用户对象
     */
    public static final Function<LoginReq, AnUser> verifyLogin =
        req -> getAuthService().verifyLogin(req.getUsername(), req.getPassword());
    /**
     * 根据用户ID获取用户信息
     * 参数: String 用户ID
     * 返回: AnUser 用户对象
     */
    public static final Function<String, AnUser> getUserById =
        id -> getAuthService().getUserById(id);
    /**
     * 获取用户权限列表
     * 参数: String 用户ID
     * 返回: List<String> 权限列表
     */
    public static final Function<String, List<String>> permissionList =
        userId -> getAuthService().permissionList(userId);
    /**
     * 获取用户角色列表
     * 参数: String 用户ID
     * 返回: List<String> 角色列表
     */
    public static final Function<String, List<String>> roleList =
        userId -> getAuthService().roleList(userId);
    /**
     * 移除用户权限角色缓存
     * 参数: String 用户ID
     */
    public static final Consumer<String> removePermRoleCacheList =
        userId -> getAuthService().removePermRoleCacheList(userId);

    /**
     * 私有构造方法防止实例化
     */
    private AuthFunction() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 获取AuthService实例
     *
     * @return AuthService实例
     */
    private static AuthService getAuthService() {
        return AnnoBeanUtils.getBean(AuthService.class);
    }
}
