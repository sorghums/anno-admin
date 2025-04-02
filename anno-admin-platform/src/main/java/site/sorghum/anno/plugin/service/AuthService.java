package site.sorghum.anno.plugin.service;

import site.sorghum.anno.plugin.ao.AnLoginLog;
import site.sorghum.anno.plugin.ao.AnUser;
import site.sorghum.anno.plugin.entity.request.UpdatePwdReq;

import java.util.List;
import java.util.Map;

/**
 * 身份验证服务
 *
 * @author Sorghum
 * @since 2023/06/27
 */
public interface AuthService {

    /**
     * 验证用户登录信息。
     *
     * @param mobile 用户的手机号码
     * @param pwd    用户的密码
     */
    AnUser verifyLogin(String mobile, String pwd);

    /**
     * 根据用户ID获取用户信息。
     *
     * @param id 用户的唯一标识符
     * @return 如果找到对应的用户，返回包含用户信息的AnUser对象；否则返回null。
     */
    AnUser getUserById(String id);

    /**
     * 根据用户ID获取用户拥有的权限列表。
     *
     * @param userId 用户的唯一标识符
     * @return 包含用户所有权限的字符串列表。如果用户不存在或权限列表为空，则返回空列表。
     */
    List<String> permissionList(String userId);

    /**
     * 根据用户ID获取用户所属的角色列表。
     *
     * @param userId 用户的唯一标识符
     * @return 包含用户所有角色的字符串列表。如果用户不存在或没有分配任何角色，则返回空列表。
     */
    List<String> roleList(String userId);

    /**
     * 移除用户权限角色缓存列表。
     * <p>根据提供的用户ID，移除与该用户相关的权限角色缓存列表。
     *
     * @param userId 用户的唯一标识符
     */
    void removePermRoleCacheList(String userId);

    /**
     * 校验是否有权限
     *
     * @param permissionCode 权限码
     * @throws site.sorghum.anno._common.exception.BizException 没有权限时，会抛出此异常
     */
    void verifyPermission(String permissionCode);

    /**
     * 保存登录日志
     */
    void saveLoginLog(AnLoginLog anLoginLog);

    /**
     * 强制注销
     */
    void forceLogout(Map<String, Object> data);

    /**
     * 更新pwd
     *
     * @param req 请求体
     */
    void updatePwd(UpdatePwdReq req);

    /**
     * 初始化菜单。
     */
    void initMenus();

    /**
     * 初始化权限。
     */
    void initPermissions();

}
