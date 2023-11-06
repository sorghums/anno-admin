package site.sorghum.anno.solon.config;

import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.serialization.snack3.SnackActionExecutor;
import org.noear.solon.serialization.snack3.SnackRenderFactory;


@Configuration
public class SnackConfig {
    @Bean
    public void jsonInit(@Inject SnackRenderFactory factory, @Inject SnackActionExecutor executor){
        // Class 类型转换为 String
        factory.addConvertor(Class.class, Class::getName);
    }
}