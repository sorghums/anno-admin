package site.sorghum.anno.pf4j;

import lombok.SneakyThrows;
import org.pf4j.PluginWrapper;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.MetadataManager;

import java.util.List;

/**
 * 加载实体类插件
 */
public abstract class Pf4jLoadEntityPlugin extends Pf4jPlugin {

    MetadataManager metadataManager;

    protected Pf4jLoadEntityPlugin(Pf4jPluginContext context) {
        super(context);
    }

    /**
     * 执行顺序，越大越先执行
     */
    public int runOrder() {
        return 10;
    }

    /**
     * 加载AnnoMain yml文件内容
     */
    public abstract List<String> ymlContents();

    /**
     * 加载AnnoMain java类
     */
    public abstract List<Class<?>> javaClasses();

    @SneakyThrows
    @Override
    public void start() {
        metadataManager = AnnoBeanUtils.getBean(MetadataManager.class);
        PluginWrapper pluginWrapper = this.context.getPluginWrapper();
        Pf4jWholeClassLoader.addClassLoader(pluginWrapper.getPluginClassLoader());
        String pluginId = pluginWrapper.getPluginId();
        log.info("[{}]加载实体类插件中", pluginId);
        List<Class<?>> classes = javaClasses();
        for (Class<?> aClass : classes) {
            metadataManager.loadEntity(aClass, true);
        }
        List<String> ymlList = ymlContents();
        for (String ymlContent : ymlList) {
            metadataManager.loadEntityListByYml(ymlContent, true);
        }
        metadataManager.refresh();
    }

    @Override
    public void stop() {
        metadataManager = AnnoBeanUtils.getBean(MetadataManager.class);
        PluginWrapper pluginWrapper = this.context.getPluginWrapper();
        Pf4jWholeClassLoader.removeClassLoader(pluginWrapper.getPluginClassLoader());
        String pluginId = pluginWrapper.getPluginId();
        log.info("[{}]停止实体类插件中", pluginId);
        List<Class<?>> classes = javaClasses();
        for (Class<?> aClass : classes) {
            metadataManager.removeEntity(aClass);
        }
        List<String> ymlList = ymlContents();
        for (String ymlContent : ymlList) {
            metadataManager.removeEntityListByYml(ymlContent);
        }
        metadataManager.refresh();
    }

    @Override
    public void delete() {
        super.delete();
    }
}
