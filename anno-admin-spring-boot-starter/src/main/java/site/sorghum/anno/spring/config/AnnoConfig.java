package site.sorghum.anno.spring.config;

import jakarta.inject.Inject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import site.sorghum.anno._common.config.AnnoProperty;

import java.beans.ConstructorProperties;

/**
 * @author songyinyin
 * @since 2023/7/30 12:16
 */
@Configuration
public class AnnoConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "anno-admin")
    public AnnoProperty annoProperty() {
        return new AnnoProperty();
    }

}
