package site.sorghum.anno.pf4j;

import lombok.SneakyThrows;
import org.pf4j.PluginWrapper;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.MetadataManager;

import java.util.List;
import java.util.Map;

/**
 * 加载实体类插件
 */
public abstract class Pf4jLoadEntityPlugin extends Pf4jPlugin {

    MetadataManager metadataManager;

    public Pf4jLoadEntityPlugin(Pf4jPluginContext context) {
        super(context);
    }

    /**
     * 执行顺序，越大越先执行
     */
    public int runOrder() {
        return 10;
    }

    /**
     * 加载AnnoMain java类
     */
    public abstract List<Class<?>> javaClasses();

    /**
     * 注册到容器的Bean
     */
    public abstract Map<String, Object> registerBeans();

    @SneakyThrows
    @Override
    public void start() {
        metadataManager = AnnoBeanUtils.getBean(MetadataManager.class);
        PluginWrapper pluginWrapper = this.context.getPluginWrapper();
        Pf4jWholeClassLoader.addClassLoader(pluginWrapper.getPluginClassLoader());
        log.info("[{}]将插件Bean注册到容器中", pluginWrapper.getPluginId());
        registerBeans().forEach(AnnoBeanUtils::registerBean);
        String pluginId = pluginWrapper.getPluginId();
        log.info("[{}]加载实体类插件中", pluginId);
        List<Class<?>> classes = javaClasses();
        for (Class<?> aClass : classes) {
            metadataManager.loadEntity(aClass, true);
        }
        metadataManager.refresh();
    }

    @Override
    public void stop() {
        metadataManager = AnnoBeanUtils.getBean(MetadataManager.class);
        PluginWrapper pluginWrapper = this.context.getPluginWrapper();
        Pf4jWholeClassLoader.removeClassLoader(pluginWrapper.getPluginClassLoader());
        String pluginId = pluginWrapper.getPluginId();
        log.info("[{}]将插件Bean从容器中移除", pluginId);
        registerBeans().forEach((k, v) -> AnnoBeanUtils.unregisterBean(k));
        log.info("[{}]停止实体类插件中", pluginId);
        List<Class<?>> classes = javaClasses();
        for (Class<?> aClass : classes) {
            metadataManager.removeEntity(aClass);
        }
        metadataManager.refresh();
    }

    @Override
    public void delete() {
        super.delete();
    }
}
