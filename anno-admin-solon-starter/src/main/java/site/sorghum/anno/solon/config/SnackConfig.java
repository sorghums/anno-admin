package site.sorghum.anno.solon.config;

import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.serialization.snack3.SnackActionExecutor;
import org.noear.solon.serialization.snack3.SnackRenderFactory;


@Configuration
public class SnackConfig {

    private static final String SPLIT = "\\.";
    @Bean
    public void jsonInit(@Inject SnackRenderFactory factory, @Inject SnackActionExecutor executor) {
        // Class 类型转换为 String
        factory.addConvertor(Class.class,
            c -> {
                if (c == null) {
                    return null;
                } else {
                    String[] names = c.getName().split(SPLIT);
                    return names[names.length - 1];
                }
            }
        );
    }
}