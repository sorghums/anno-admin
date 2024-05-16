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

    AnUser verifyLogin(String mobile, String pwd);

    AnUser getUserById(String id);

    List<String> permissionList(String userId);

    List<String> roleList(String userId);

    void removePermRoleCacheList(String userId);

    /**
     * 校验是否有权限
     *
     * @param permissionCode 权限码
     * @throws site.sorghum.anno._common.exception.BizException 没有权限时，会抛出此异常
     */
    void verifyPermission(String permissionCode);


    /**
     * 重置pwd
     *
     * @param data 数据
     */
    void resetPwd(Map<String,Object> data);

    /**
     * 校验是否有按钮权限
     */
    void verifyButtonPermission(String className, String methodName);

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
}
