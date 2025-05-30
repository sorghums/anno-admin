package site.sorghum.anno.solon.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.dao.SaTokenDaoForRedisx;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.solon.integration.SaTokenInterceptor;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Condition;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno.anno.functions.AnnoFunction;

/**
 * @author Sorghum
 */
@Configuration
public class SaConfig {

    @Bean
    @Condition(onProperty = "${anno-admin.class.SaTokenInterceptor:true} = true", onClassName = "org.noear.redisx.RedisClient")
    public SaTokenDao saTokenDaoInit(@Inject("${anno-admin.redis}") SaTokenDaoForRedisx saTokenDao) {
        return saTokenDao;
    }

    @Condition(onMissingBean = SaTokenDao.class)
    public SaTokenDao localSaTokenDaoInit() {
        return SaManager.getSaTokenDao();
    }

    @Bean(index = -100)  //-100，是顺序位（低值优先）
    @Condition(onProperty = "${anno-admin.class.SaTokenInterceptor:true} = true")
    public SaTokenInterceptor saTokenInterceptor() {
        return new SaTokenInterceptor()
            // [拦截路由]
            .addInclude(AnnoConstants.BASE_URL + "/**")
            // [放行路由]
            .addExclude("/favicon.ico", "/doc.html", "/swagger-resources", "/swagger/*")
            // 认证函数: 每次请求执行
            .setAuth(req -> SaRouter.match(AnnoConstants.BASE_URL + "/**", () -> AnnoFunction.loginCheckFunction.run()));
    }
}