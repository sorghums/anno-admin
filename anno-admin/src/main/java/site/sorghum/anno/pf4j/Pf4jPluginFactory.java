package site.sorghum.anno.pf4j;

import org.pf4j.DefaultPluginFactory;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public class Pf4jPluginFactory extends DefaultPluginFactory {

    private static final Logger log = LoggerFactory.getLogger(Pf4jPluginFactory.class);

    @Override
    protected Plugin createInstance(Class<?> pluginClass, PluginWrapper pluginWrapper) {
        Pf4jPluginContext context = new Pf4jPluginContext(pluginWrapper);
        try {
            Constructor<?> constructor = pluginClass.getConstructor(Pf4jPluginContext.class);
            return (Plugin) constructor.newInstance(context);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }





}