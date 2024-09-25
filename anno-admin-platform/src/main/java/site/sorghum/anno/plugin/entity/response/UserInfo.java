package site.sorghum.anno.plugin.entity.response;

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

    String homePath;

    List<String> roles;

    List<String> perms;
}
