package site.sorghum.anno.pf4j;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.DefaultPluginManager;
import org.pf4j.ExtensionWrapper;
import org.pf4j.PluginFactory;
import org.pf4j.PluginRuntimeException;
import site.sorghum.anno._common.AnnoBeanUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    @Override
    protected <T> List<T> getExtensions(List<ExtensionWrapper<T>> extensionsWrapper) {
        List<T> extensions = new ArrayList<>(extensionsWrapper.size());
        for (ExtensionWrapper<T> extensionWrapper : extensionsWrapper) {
            try {
                extensions.add(extensionWrapper.getExtension());
            } catch (PluginRuntimeException e) {
                log.error("Cannot retrieve extension", e);
            }
        }
        for (T extension : extensions) {
            String beanName = StrUtil.lowerFirst(extension.getClass().getSimpleName());
            AnnoBeanUtils.registerBean(beanName, extension);
        }
        return extensions;
    }
}