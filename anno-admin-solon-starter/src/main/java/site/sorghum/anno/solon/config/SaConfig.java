package site.sorghum.anno.solon.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.filter.SaFilterAuthStrategy;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.solon.dao.SaTokenDaoOfRedis;
import cn.dev33.satoken.solon.integration.SaTokenInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.noear.redisx.RedisClient;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Condition;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno._common.response.AnnoResult;

/**
 * @author Sorghum
 */
@Configuration
public class SaConfig {

    @Bean
    @Condition(onProperty = "${anno-admin.class.SaTokenInterceptor:true} = true")
    public SaTokenDao saTokenDaoInit(@Inject("${anno-admin.redis}") SaTokenDaoOfRedis saTokenDao) {
        return saTokenDao;
    }

    @Bean
    public RedisClient redisClient(@Inject("${anno-admin.redis}") RedisClient client) {
        return client;
    }

    @Bean(index = -100)  //-100，是顺序位（低值优先）
    @Condition(onProperty = "${anno-admin.class.SaTokenInterceptor:true} = true")
    public SaTokenInterceptor saTokenInterceptor() {
        return new SaTokenInterceptor()
            // [拦截路由]
            .addInclude("/**")
            // [放行路由]
            .addExclude("/favicon.ico","/doc.html", "/swagger-resources", "/swagger/*")
            .addExclude("/solon-admin/api/**")
            // 认证函数: 每次请求执行
            .setAuth(req -> SaRouter.match("/**", StpUtil::checkLogin))
            .setBeforeAuth(
                    req -> {
                        SaResponse response = SaHolder.getResponse();
                        System.out.println(response);
                    }
            );
    }
}