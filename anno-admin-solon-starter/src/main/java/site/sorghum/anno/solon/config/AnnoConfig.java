package site.sorghum.anno.solon.config;

import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import site.sorghum.anno._common.config.AnnoProperty;

/**
 * @author songyinyin
 * @since 2023/7/30 12:16
 */
@Configuration
public class AnnoConfig {

    @Bean(typed = true)
    public AnnoProperty annoProperty(@Inject(value = "${anno-admin}", required = false) AnnoProperty annoProperty) {
        return annoProperty == null ? new AnnoProperty() : annoProperty;
    }

}
