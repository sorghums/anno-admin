package site.sorghum.anno;


import lombok.extern.slf4j.Slf4j;
import org.pf4j.Extension;
import site.sorghum.anno.plugin.AnnoPlugin;

/**
 * 基础模块
 *
 * @author sorghum
 * @since 2023/07/15
 */
@Slf4j
@Extension
public class AnnoCorePlugin extends AnnoPlugin {

    public AnnoCorePlugin() {
        super("核心插件", "核心功能模块");
    }

    @Override
    public int runOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void run() {}
}
