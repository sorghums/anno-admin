package site.sorghum.anno.common.config;

import cn.hutool.core.util.ReflectUtil;
import lombok.SneakyThrows;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Init;
import org.noear.solon.view.thymeleaf.ThymeleafRender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.serializer.IStandardJavaScriptSerializer;
import site.sorghum.anno.common.util.JSONUtil;

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

    static class FastJson2Serializer implements IStandardJavaScriptSerializer {
        @SneakyThrows
        @Override
        public void serializeValue(Object object, Writer writer) {
            writer.write(JSONUtil.toJsonString(object));
        }
    }

    @Init
    public void init() {
        ThymeleafRender thymeleafRender = ThymeleafRender.global();
        TemplateEngine templateEngine = (TemplateEngine) ReflectUtil.getFieldValue(thymeleafRender, "provider");
        Set<IDialect> dialects =
                templateEngine.getDialects();
        dialects.stream()
                .filter(dialect -> dialect instanceof StandardDialect)
                .findAny()
                .ifPresent(dialect -> {
                    ((StandardDialect) dialect).setJavaScriptSerializer(new FastJson2Serializer());
                });
    }
}
