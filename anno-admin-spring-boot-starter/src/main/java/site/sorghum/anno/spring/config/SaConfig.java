package site.sorghum.anno.spring.config;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.dao.SaTokenDaoOfRedis;
import cn.dev33.satoken.interceptor.SaInterceptor;
import org.noear.redisx.RedisClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno.auth.AnnoStpUtil;

import java.util.Properties;

/**
 * @author Sorghum
 */
@Configuration
public class SaConfig implements WebMvcConfigurer {


    @Bean
    public SaTokenDao saTokenDaoInit(Properties redisClientProperties) {
        return new SaTokenDaoOfRedis(redisClientProperties);
    }


    @Bean
    @ConfigurationProperties(prefix = "anno-admin.redis")
    public Properties redisClientProperties() {
        return new Properties();
    }

    ;

    @Bean
    public RedisClient redisClient(Properties redisClientProperties) {
        return new RedisClient(redisClientProperties);
    }

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> AnnoStpUtil.checkLogin()))
            .addPathPatterns(AnnoConstants.BASE_URL + "/**")
            .excludePathPatterns(AnnoConstants.BASE_URL + "/favicon.ico")
            .excludePathPatterns(AnnoConstants.BASE_URL + "/_app.config.js")
            .excludePathPatterns(AnnoConstants.BASE_URL + "/logo.png")
            .excludePathPatterns(AnnoConstants.BASE_URL + "/assets/**")
            .excludePathPatterns(AnnoConstants.BASE_URL + "/index.html");
    }

}