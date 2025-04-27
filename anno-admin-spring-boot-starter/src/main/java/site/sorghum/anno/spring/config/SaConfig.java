package site.sorghum.anno.spring.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.dao.SaTokenDaoForRedisx;
import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno.anno.functions.AnnoFunction;

import java.util.Properties;

/**
 * @author Sorghum
 */
@Configuration
public class SaConfig implements WebMvcConfigurer {


    @Bean
    @ConditionalOnClass(name = "org.noear.redisx.RedisClient")
    public SaTokenDao saTokenDaoInit(Properties redisClientProperties) {
        return new SaTokenDaoForRedisx(redisClientProperties);
    }

    @ConditionalOnMissingBean(value = SaTokenDao.class)
    public SaTokenDao localSaTokenDaoInit() {
        return SaManager.getSaTokenDao();
    }


    @Bean
    @ConfigurationProperties(prefix = "anno-admin.redis")
    public Properties redisClientProperties() {
        return new Properties();
    }

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle ->  AnnoFunction.loginCheckFunction.run()))
            .addPathPatterns(AnnoConstants.BASE_URL + "/**")
            .excludePathPatterns(AnnoConstants.BASE_URL + "/favicon.ico")
            .excludePathPatterns(AnnoConstants.BASE_URL + "/_app.config.js")
            .excludePathPatterns(AnnoConstants.BASE_URL + "/logo.png")
            .excludePathPatterns(AnnoConstants.BASE_URL + "/assets/**")
            .excludePathPatterns(AnnoConstants.BASE_URL + "/index.html");
    }

    @Bean
    @ConditionalOnMissingBean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 允许所有域名跨域访问
        config.addAllowedOrigin("*");
        // 允许跨域请求的方法
        config.addAllowedMethod("*");
        // 允许跨域请求包含的头信息
        config.addAllowedHeader("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}