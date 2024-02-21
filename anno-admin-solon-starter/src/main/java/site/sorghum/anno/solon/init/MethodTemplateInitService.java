package site.sorghum.anno.solon.init;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import site.sorghum.anno.method.MethodTemplateManager;

import java.io.IOException;
import java.util.Set;

/**
 * 方法模板初始化服务
 *
 * @author Sorghum
 * @since 2024/02/21
 */
@Component
public class MethodTemplateInitService {

    /**
     * 扫描的包
     */
    public static Set<String> packages;

    @Init
    public void init() throws IOException {
        // 方法模版初始化
        for (String annoPackage : packages) {
            MethodTemplateManager.parse(annoPackage);
        }
    }
}
