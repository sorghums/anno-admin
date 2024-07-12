package site.sorghum.anno;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.WoodConfig;
import org.noear.wood.wrap.NamingStrategy;
import org.noear.wood.wrap.PrimaryKeyStrategy;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.util.AnnoContextUtil;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.util.AnnoFieldCache;
import site.sorghum.anno.anno.util.ReentrantStopWatch;
import site.sorghum.anno.db.service.wood.AnnoPrimaryKeyStrategy;
import site.sorghum.anno.plugin.AnnoPlugin;

import java.lang.reflect.Field;

/**
 * 基础模块
 *
 * @author sorghum
 * @since 2023/07/15
 */
@Slf4j
@Named
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
