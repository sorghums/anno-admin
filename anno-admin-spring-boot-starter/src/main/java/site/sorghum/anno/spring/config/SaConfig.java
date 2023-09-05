package site.sorghum.anno.spring.config;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.dao.SaTokenDaoOfRedis;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.noear.redisx.RedisClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
            .addPathPatterns("/**")
            .excludePathPatterns("/favicon.ico")
            .excludePathPatterns("/assets/**")
            .excludePathPatterns("/index.html");
    }

}