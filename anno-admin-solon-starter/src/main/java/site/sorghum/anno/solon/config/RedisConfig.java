package site.sorghum.anno.solon.config;

import org.noear.redisx.RedisClient;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Condition;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;

import java.util.Properties;

@Configuration
@Condition(onClassName = "org.noear.redisx.RedisClient")
public class RedisConfig {

    @Bean
    public RedisClient redisClient(@Inject("${anno-admin.redis}") RedisClient redisClient) {
        return redisClient;
    }

}
