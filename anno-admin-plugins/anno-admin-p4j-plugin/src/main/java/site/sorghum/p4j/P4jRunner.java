package site.sorghum.p4j;

import cn.hutool.core.io.FileUtil;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

import java.nio.file.Path;
import java.util.List;

public class P4jRunner {
    PluginManager pluginManager = new DefaultPluginManager(Path.of(tempPath()));

    public static void main(String[] args) {
        P4jRunner p4jRunner = new P4jRunner();
        p4jRunner.start();
        p4jRunner.exit();
    }
    public static String tempPath(){
        String tempPath = FileUtil.getTmpDirPath() + "anno-admin/p4j-plugins";
        if (FileUtil.exist(tempPath)){
            return tempPath;
        }
        FileUtil.mkdir(tempPath);
        return tempPath;
    }
    public void start() {
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
        List<AnnoAdminPf4j> extensions = pluginManager.getExtensions(AnnoAdminPf4j.class);
        System.out.println(extensions);
        pluginManager.getPlugins().forEach(System.out::println);
    }

    public void exit(){
        pluginManager.stopPlugins();
        pluginManager.unloadPlugins();
    }
}
