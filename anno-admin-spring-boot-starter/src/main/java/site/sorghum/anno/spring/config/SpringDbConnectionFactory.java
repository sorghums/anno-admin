package site.sorghum.anno.spring.config;

import org.noear.wood.DbConnectionFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 集成 spring 事务
 *
 * @author songyinyin
 * @since 2024/2/29 21:15
 */
public class SpringDbConnectionFactory extends DbConnectionFactory {

    @Override
    public Connection getConnection(DataSource ds) throws SQLException {
        return DataSourceUtils.getConnection(ds);
    }
}
