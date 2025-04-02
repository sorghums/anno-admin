package site.sorghum.anno.plugin;

import jakarta.inject.Named;
import site.sorghum.anno.pf4j.Pf4jRunner;

import java.util.Comparator;
import java.util.List;

/**
 * 插件运行程序
 *
 * @author Sorghum
 * @since 2023/12/20
 */
@Named
public class PluginRunner {

    public volatile boolean init;

    public synchronized void init() {
        if (init){
            return;
        }
        List<AnnoPlugin> annoPlugins = Pf4jRunner.PLUGIN_MANAGER.getExtensions(
            AnnoPlugin.class
        );
        annoPlugins.sort(Comparator.comparingInt(AnnoPlugin::runOrder).reversed());
        annoPlugins.forEach(AnnoPlugin::printPluginInfo);
        annoPlugins.forEach(AnnoPlugin::run);
        init = true;
    }

}
