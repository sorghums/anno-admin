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
    public  Properties redisClientProperties(){
        return new Properties();
    };

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
                .excludePathPatterns("/favicon.ico");
    }
//
//    @Bean(index = -100)  //-100，是顺序位（低值优先）
//    public SaTokenInterceptor saTokenInterceptor() {
//        return new SaTokenInterceptor()
//                // 指定 [拦截路由] 与 [放行路由]
//                .addInclude("/**").addExclude("/favicon.ico")
//                // 认证函数: 每次请求执行
//                .setAuth(req -> {
//                    SaRouter.match("/**", StpUtil::checkLogin);
//                })
//                // 异常处理函数：每次认证函数发生异常时执行此函数 //包括注解异常
//                .setError(e -> {
//                    return AnnoResult.failure(e.getMessage());
//                })
//                // 前置函数：在每次认证函数之前执行
//                .setBeforeAuth(req -> {
//                    // ---------- 设置一些安全响应头 ----------
//                    SaHolder.getResponse()
//                            // 服务器名称
//                            .setServer("anno-server")
//                            // 是否可以在iframe显示视图： DENY=不可以 | SAMEORIGIN=同域下可以 | ALLOW-FROM uri=指定域名下可以
//                            .setHeader("X-Frame-Options", "SAMEORIGIN")
//                            .addHeader("Access-Control-Allow-Origin", SaHolder.getRequest().getHeader("Origin"))
//                            .addHeader("Access-Control-Allow-Methods", "*")
//                            .addHeader("Access-Control-Allow-Headers", "anno-token,content-type,*")
//                            .addHeader("Access-Control-Allow-Credentials", "true")
//                            // 是否启用浏览器默认XSS防护： 0=禁用 | 1=启用 | 1; mode=block 启用, 并在检查到XSS攻击时，停止渲染页面
//                            .setHeader("X-XSS-Protection", "1; mode=block")
//                            // 禁用浏览器内容嗅探
//                            .setHeader("X-Content-Type-Options", "nosniff");
//                });
//    }
}