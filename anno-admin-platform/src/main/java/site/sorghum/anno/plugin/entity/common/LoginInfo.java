package site.sorghum.anno.plugin.entity.common;

import lombok.Builder;
import lombok.Data;

/**
 * 登录信息
 *
 * @author Qianjiawei
 * @since 2024/01/04
 */
@Data
@Builder
public class LoginInfo {

    String ip;

    String userAgent;
}
