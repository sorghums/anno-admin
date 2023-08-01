package site.sorghum.anno.spring.config;

import org.noear.wood.DbContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 配置
 *
 * @author Sorghum
 * @since 2023/08/01
 */
@Configuration
public class SpringWoodConfig {
    @Bean
    DbContext dbContext(DataSource dataSource){
        return new DbContext(dataSource);
    }
}
