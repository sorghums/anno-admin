package site.sorghum.anno.pf4j;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.*;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno.plugin.AnnoPlugin;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class Pf4jPluginManager extends DefaultPluginManager {
    // getExtensions 是否初始化过
    private volatile boolean init = false;

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
        // 兼容旧版本 @Component 注解
        if (!init && !extensionsWrapper.isEmpty() && extensionsWrapper.get(0).getExtension() instanceof AnnoPlugin) {
            List<AnnoPlugin> beans = AnnoBeanUtils.getBeansOfType(AnnoPlugin.class);
            extensions.addAll((Collection<? extends T>) beans);
            init = true;
        }
        for (ExtensionWrapper<T> extensionWrapper : extensionsWrapper) {
            try {
                extensions.add(extensionWrapper.getExtension());
            } catch (PluginRuntimeException e) {
                log.error("Cannot retrieve extension", e);
            }
        }
        for (T extension : extensions) {
            String beanName = StrUtil.lowerFirst(extension.getClass().getSimpleName());
            Object bean = AnnoBeanUtils.getBean(beanName);
            if (bean != null) {
                continue;
            }
            AnnoBeanUtils.registerBean(beanName, extension);
        }
        return extensions;
    }

    @Override
    protected PluginLoader createPluginLoader() {
        return new CompoundPluginLoader()
            .add(new JarPluginLoader(this))
            .add(new DevelopmentPluginLoader(this))
            .add(new DefaultPluginLoader(this));
    }
}