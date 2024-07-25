package site.sorghum.anno.pf4j;

import org.pf4j.Plugin;

/**
 * pf4j插件
 *
 * @author Sorghum
 * @since 2024/07/25
 */
abstract class Pf4jPlugin extends Plugin {

    protected final Pf4jPluginContext context;

    protected Pf4jPlugin(Pf4jPluginContext context) {
        super();
        this.context = context;
    }

}