package site.sorghum.anno.pf4j;

import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginFactory;

import java.nio.file.Path;
import java.util.List;

public class Pf4jPluginManager extends DefaultPluginManager {

    public Pf4jPluginManager() {
    }

    public Pf4jPluginManager(Path... pluginsRoots) {
        super(pluginsRoots);
    }

    public Pf4jPluginManager(List<Path> pluginsRoots) {
        super(pluginsRoots);
    }

    @Override
    protected PluginFactory createPluginFactory() {
        return new Pf4jPluginFactory();
    }
}