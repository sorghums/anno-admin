package site.sorghum.anno.spring.config;

import org.noear.wood.DbContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno.spring.auth.StpInterfaceImpl;

import javax.sql.DataSource;

/**
 * @author songyinyin
 * @since 2023/7/30 12:16
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {AnnoConfig.ANNO_BASE_PACKAGE, AnnoConfig.PLUGIN_BASE_PACKAGE})
public class AnnoConfig {
    public static final String ANNO_BASE_PACKAGE = "site.sorghum.anno";
    public static final String PLUGIN_BASE_PACKAGE = "site.sorghum.plugin";

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "anno-admin")
    public AnnoProperty annoProperty() {
        return new AnnoProperty();
    }

    @Bean
    @ConditionalOnMissingBean
    public DbContext dbContext(DataSource dataSource) {
        DbContext dbContext = new DbContext(dataSource);
        dbContext.nameSet(AnnoConstants.DEFAULT_DATASOURCE_NAME);
        return dbContext;
    }

    @Bean
    @ConditionalOnMissingBean
    public StpInterfaceImpl stpInterfaceImpl() {
        return new StpInterfaceImpl();
    }

}
