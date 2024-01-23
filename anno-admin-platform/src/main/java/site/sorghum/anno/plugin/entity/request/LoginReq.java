package site.sorghum.anno.plugin.entity.request;

import lombok.Data;

@Data
public class LoginReq {

    String username;

    String password;

    String codeKey;

    String code;
}
