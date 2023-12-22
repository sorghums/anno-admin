package site.sorghum.anno.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Anno验证用户
 *
 * @author Sorghum
 * @since 2023/12/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnoAuthUser implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    String userId;

    /**
     * 用户名
     */
    String userName;

    /**
     * 用户帐户
     */
    String userAccount;

    /**
     * 用户手机
     */
    String userMobile;

    /**
     * 用户状态
     */
    String userStatus;

    /**
     * 组织id
     */
    String orgId;
}
