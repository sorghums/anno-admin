package site.sorghum.anno.modular.auth.response;

import lombok.Data;

import java.util.List;

/**
 * 用户信息
 *
 * @author sorghum
 * @date 2023/07/23
 */
@Data
public class UserInfo {

    String name;

    String avatar;

    List<String> roles;

    List<String> perms;
}
