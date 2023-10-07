package site.sorghum.anno;


import cn.hutool.core.util.ArrayUtil;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.noear.dami.Dami;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.anno.proxy.DbServiceWithProxy;
import site.sorghum.anno.db.interfaces.AnnoAdminCoreFunctions;
import site.sorghum.anno.plugin.AnnoPlugin;

import java.util.List;

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
    public void run() {
        AnnoAdminCoreFunctions.tableParamFetchFunction = AnnoBeanUtils.metadataManager()::getTableParam;

        // 将所有代理，注册到 dami 的监听器中
        List<AnnoBaseProxy> proxies = AnnoBeanUtils.getBeansOfType(AnnoBaseProxy.class);
        for (AnnoBaseProxy<?> proxy : proxies) {
            if (ArrayUtil.isEmpty(proxy.supportEntities())) {
                continue;
            }
            for (String entityName : proxy.supportEntities()) {
                Dami.api().registerListener(DbServiceWithProxy.BASE_ENTITY_TOPIC + entityName + "**", proxy.index(), proxy);
            }
        }
    }
}
