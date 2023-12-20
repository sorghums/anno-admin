package site.sorghum.anno.plugin;

import jakarta.inject.Named;
import site.sorghum.anno._common.AnnoBeanUtils;

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

    public void init() {
        List<AnnoPlugin> annoPlugins = AnnoBeanUtils.getBeansOfType(AnnoPlugin.class);
        annoPlugins.sort(Comparator.comparingInt(AnnoPlugin::runOrder).reversed());
        annoPlugins.forEach(AnnoPlugin::printPluginInfo);
        annoPlugins.forEach(AnnoPlugin::run);
    }

}
