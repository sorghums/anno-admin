package site.sorghum.anno.spring.config;

import org.noear.redisx.RedisClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@ConditionalOnClass(name = "org.noear.redisx.RedisClient")
public class RedisConfig {


    @Bean
    public RedisClient redisClient(Properties redisClientProperties) {
        return new RedisClient(redisClientProperties);
    }
}
