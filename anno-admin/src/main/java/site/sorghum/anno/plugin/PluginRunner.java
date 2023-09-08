package site.sorghum.anno.plugin;

import jakarta.inject.Named;
import site.sorghum.anno._common.AnnoBeanUtils;

import java.util.List;

@Named
public class PluginRunner {

    public void init() {
        List<AnnoPlugin> annoPlugins = AnnoBeanUtils.getBeansOfType(AnnoPlugin.class);
        annoPlugins.forEach(AnnoPlugin::printPluginInfo);
        annoPlugins.forEach(AnnoPlugin::run);
    }

}
