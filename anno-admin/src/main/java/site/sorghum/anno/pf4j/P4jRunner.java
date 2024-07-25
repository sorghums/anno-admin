package site.sorghum.anno.pf4j;

import cn.hutool.core.io.FileUtil;
import jakarta.inject.Named;
import org.pf4j.PluginManager;

import java.nio.file.Path;

@Named
public class P4jRunner {

    /**
     * 插件管理器
     */
    public static PluginManager PLUGIN_MANAGER = new Pf4jPluginManager(Path.of(tempPath()));


    public P4jRunner() {
        PLUGIN_MANAGER.loadPlugins();
        PLUGIN_MANAGER.startPlugins();
    }

    public static String tempPath() {
        String tempPath = FileUtil.getTmpDirPath() + "anno-admin/p4j-plugins";
        if (FileUtil.exist(tempPath)) {
            return tempPath;
        }
        FileUtil.mkdir(tempPath);
        return tempPath;
    }
}
