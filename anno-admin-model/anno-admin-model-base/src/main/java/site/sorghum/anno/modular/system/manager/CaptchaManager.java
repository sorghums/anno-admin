package site.sorghum.anno.modular.system.manager;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.redisx.RedisClient;
import org.noear.redisx.plus.RedisBucket;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno.common.exception.BizException;
import site.sorghum.anno.modular.system.entity.response.CaptchaResponse;

import java.util.Objects;

/**
 * 验证码管理
 *
 * @author Sorghum
 * @since 2023/04/28
 */
@Component
@Slf4j
public class CaptchaManager {
    @Inject("${sgm.captcha.enable:true}")
    boolean enable;

    RedisBucket bucket;

    @Init
    private void init() {
        Solon.context().getBeanAsync(RedisClient.class, redisClient -> {
            bucket = redisClient.getBucket();
        });
    }

    /**
     * 创建图片验证码
     *
     * @return {@link String}
     */
    public CaptchaResponse createImageCaptcha() {
        // 随机缓存key
        String key = RandomUtil.randomString(5);
        // 生成图片验证码
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(130, 48, 4, 4);
        // 缓存验证码
        addCacheCode(key, captcha.getCode());
        // 返回验证码图片
        return new CaptchaResponse(key, captcha.getImageBase64Data());
    }

    /**
     * 创建手机验证码
     *
     * @return {@link String}
     */
    public CaptchaResponse createMobileCaptcha(String mobile) {
        // 随机缓存key
        String key = RandomUtil.randomString(5);
        // 随机生成4位 数字码
        String code = RandomUtil.randomString(RandomUtil.BASE_NUMBER, 4);
        // 发送验证码
        log.info("发送验证码：{}，{}", mobile, code);
        // 缓存验证码
        addCacheCode(key, code, 5 * 60);
        // 返回验证码
        return new CaptchaResponse(key, null);
    }

    /**
     * 验证验证码
     *
     * @param captchaKey  缓存key
     * @param captchaCode 前端验证码
     */
    public void verifyCaptcha(String captchaKey, String captchaCode) {
        // 如果验证码开关关闭则不验证
        if (Objects.equals(enable, false)) {
            return;
        }
        String code = getCache(captchaKey);
        if (!captchaCode.equals(code)) {
            throw new BizException("验证码错误");
        }
    }

    /**
     * 固定时间60s验证码缓存
     *
     * @param key  缓存key
     * @param code 验证码
     */
    private void addCacheCode(String key, String code) {
        addCacheCode(key, String.valueOf(code), 60);
    }

    /**
     * 验证码缓存
     *
     * @param key     缓存key
     * @param code    验证码
     * @param seconds 秒
     */
    private void addCacheCode(String key, String code, Integer seconds) {
        log.debug("验证码新增缓存key:{},code:{}", key, code);
        putCache(key, code, seconds);
    }

    private void putCache(String key, String code, Integer seconds) {
        bucket.store("anno-admin:captcha:admin:" +key, code, seconds);
    }

    private String getCache(String key) {
        return bucket.get("anno-admin:captcha:admin:" +key);
    }

}
