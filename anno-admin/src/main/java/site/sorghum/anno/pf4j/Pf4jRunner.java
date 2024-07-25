package site.sorghum.anno.pf4j;

import lombok.extern.slf4j.Slf4j;
import org.pf4j.PluginManager;
import site.sorghum.anno.plugin.AnnoPlugin;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class Pf4jRunner {

    /**
     * 插件管理器
     */
    public static PluginManager PLUGIN_MANAGER = new Pf4jPluginManager();


    public Pf4jRunner() {
        PLUGIN_MANAGER.loadPlugins();
        PLUGIN_MANAGER.startPlugins();
    }


    /**
     * 加载并运行插件
     *
     * @param path 插件文件所在的路径
     * @return 插件ID
     */
    public static String runPlugin(Path path) {
        String pluginId = PLUGIN_MANAGER.loadPlugin(path);
        PLUGIN_MANAGER.startPlugin(pluginId);
        log.info("[{}]加载实体类拓展点中", pluginId);
        List<AnnoPlugin> annoPlugins = PLUGIN_MANAGER.getExtensions(AnnoPlugin.class, pluginId);
        annoPlugins.sort(Comparator.comparingInt(AnnoPlugin::runOrder).reversed());
        annoPlugins.forEach(AnnoPlugin::printPluginInfo);
        annoPlugins.forEach(AnnoPlugin::run);
        return pluginId;
    }

    /**
     * 停止并删除指定插件
     *
     * @param pluginId 插件ID
     * @return 停止并删除的插件ID
     */
    public static String stopPlugin(String pluginId) {
        PLUGIN_MANAGER.stopPlugin(pluginId);
        PLUGIN_MANAGER.deletePlugin(pluginId);
        return pluginId;
    }
}
