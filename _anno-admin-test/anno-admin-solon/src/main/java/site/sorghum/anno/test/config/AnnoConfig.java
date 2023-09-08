package site.sorghum.anno.test.config;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno._common.AnnoConstants;

import javax.sql.DataSource;

/**
 * 数据库配置
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Configuration
public class AnnoConfig {
    @Bean(name = AnnoConstants.DEFAULT_DATASOURCE_NAME, typed = true)
    public DataSource dataSource(@Inject("${db.main}") HikariDataSource ds) {
        return ds;
    }

}
