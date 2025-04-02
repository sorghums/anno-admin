package site.sorghum.anno.plugin.entity.request;

import lombok.Data;

/**
 * 用户登录请求实体
 * <p>
 * 用于接收前端传递的用户登录凭证信息
 */
@Data
public class LoginReq {
    /**
     * 登录用户名
     * <p>要求：必填，系统唯一标识</p>
     */
    private String username;

    /**
     * 登录密码
     * <p>要求：必填，需符合系统密码复杂度规则</p>
     */
    private String password;

    /**
     * 验证码唯一标识
     * <p>说明：用于验证码校验时匹配对应验证码</p>
     */
    private String codeKey;

    /**
     * 用户输入的验证码
     * <p>要求：当系统开启验证码校验时必填</p>
     */
    private String code;
}