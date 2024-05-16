package site.sorghum.anno.plugin.entity.request;

import lombok.Data;

@Data
public class UpdatePwdReq {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 旧pwd
     */
    private String oldPwd;
    /**
     * 新pwd1
     */
    private String newPwd1;
    /**
     * 新pwd2
     */
    private String newPwd2;
}
