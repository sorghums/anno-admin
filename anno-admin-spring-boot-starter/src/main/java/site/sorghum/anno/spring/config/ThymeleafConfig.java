package site.sorghum.anno.spring.config;

import cn.hutool.core.util.ReflectUtil;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import site.sorghum.anno._common.config.AnnoProperty;
import site.sorghum.anno._common.util.JSONUtil;

import java.io.Writer;
import java.util.Set;

/**
 * thymeleaf配置
 *
 * @author sorghum
 * @since 2023/07/01
 */
@Configuration
public class ThymeleafConfig {
//
//    static class FastJson2Serializer implements IStandardJavaScriptSerializer {
//        @SneakyThrows
//        @Override
//        public void serializeValue(Object object, Writer writer) {
//            writer.write(JSONUtil.toJsonString(object));
//        }
//    }
//
//    @Inject
//    AnnoProperty annoProperty;
//
//    @Init
//    public void init() {
//        ThymeleafRender thymeleafRender = ThymeleafRender.global();
//        TemplateEngine templateEngine = (TemplateEngine) ReflectUtil.getFieldValue(thymeleafRender, "provider");
//        Set<IDialect> dialects =
//                templateEngine.getDialects();
//        dialects.stream()
//                .filter(dialect -> dialect instanceof StandardDialect)
//                .findAny()
//                .ifPresent(dialect -> {
//                    ((StandardDialect) dialect).setJavaScriptSerializer(new FastJson2Serializer());
//                });
//
//        ThymeleafRender.global().putVariable("anno_theme", annoProperty.getTheme());
//    }
}
