package site.sorghum.anno.ddl.app.config;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;

import javax.sql.DataSource;

/**
 * @author songyinyin
 * @since 2023/7/4 11:59
 */
@Configuration
public class TestConfig {

  @Bean(name = "dataSource", typed = true)
  public DataSource dataSource(@Inject("${db.main}") HikariDataSource ds) {
    return ds;
  }
}
