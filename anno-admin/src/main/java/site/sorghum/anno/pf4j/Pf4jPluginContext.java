package site.sorghum.anno.pf4j;

import lombok.Getter;
import org.pf4j.RuntimeMode;

/**
 * 插件上下文
 *
 * @author Sorghum
 * @since 2024/07/25
 */
@Getter
public class Pf4jPluginContext {

    private final RuntimeMode runtimeMode;

    public Pf4jPluginContext(RuntimeMode runtimeMode) {
        this.runtimeMode = runtimeMode;
    }

}
