package site.sorghum.anno.pre.plugin.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证码反应
 *
 * @author Sorghum
 * @since 2023/05/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaResponse {
    /**
     * 随机码Key
     */
    String key;

    /**
     * 验证码图片 Base64
     */
    String image;
}