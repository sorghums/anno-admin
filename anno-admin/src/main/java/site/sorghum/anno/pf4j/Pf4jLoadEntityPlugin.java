package site.sorghum.anno.pf4j;

import org.pf4j.Plugin;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._metadata.MetadataManager;

import java.util.List;

public abstract class Pf4jLoadEntityPlugin extends Plugin {

    MetadataManager metadataManager;

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

    @Override
    public void start() {
        metadataManager = AnnoBeanUtils.getBean(MetadataManager.class);
        List<Class<?>> classes = javaClasses();
        for (Class<?> aClass : classes) {
            metadataManager.loadEntity(aClass,true);
        }
        List<String> ymlList = ymlContents();
        for (String ymlContent : ymlList) {
            metadataManager.loadEntityListByYml(ymlContent,true);
        }
        metadataManager.refresh();

    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void delete() {
        super.delete();
    }
}
