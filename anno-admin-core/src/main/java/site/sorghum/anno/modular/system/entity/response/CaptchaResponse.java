package site.sorghum.anno.modular.system.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.noear.solon.annotation.Note;

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
    @Note("随机码Key")
    String key;

    /**
     * 验证码图片 Base64
     */
    @Note("验证码图片 Base64")
    String image;
}