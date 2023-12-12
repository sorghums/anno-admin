package site.sorghum.anno;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.noear.dami.Dami;
import org.noear.wood.WoodConfig;
import org.noear.wood.wrap.ClassWrap;
import org.noear.wood.wrap.NamingStrategy;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.util.AnnoContextUtil;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno.anno.proxy.AnnoBaseProxy;
import site.sorghum.anno.anno.proxy.DbServiceWithProxy;
import site.sorghum.anno.anno.util.AnnoFieldCache;
import site.sorghum.anno.anno.util.ReentrantStopWatch;
import site.sorghum.anno.db.interfaces.AnnoAdminCoreFunctions;
import site.sorghum.anno.plugin.AnnoPlugin;

import java.lang.reflect.Field;
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
    public int runOrder() {
        return 1000;
    }

    @Override
    public void run() {
        AnnoAdminCoreFunctions.tableParamFetchFunction = AnnoBeanUtils.metadataManager()::getTableParam;
        AnnoAdminCoreFunctions.javaField2DbFieldFunction = AnnoFieldCache::getSqlColumnByJavaName;
        AnnoAdminCoreFunctions.dbField2JavaFieldFunction = AnnoFieldCache::getFieldBySqlColumn;
        AnnoAdminCoreFunctions.fieldCanClearFunction = (clazz,fieldName) -> {
            AnEntity entity = AnnoBeanUtils.metadataManager().getEntity(clazz);
            return entity.getFieldMap().get(fieldName).isEditCanClear();
        };
        AnnoAdminCoreFunctions.sqlFieldCanClearFunction = (clazz,fieldName) -> {
            AnEntity entity = AnnoBeanUtils.metadataManager().getEntity(clazz);
            return entity.getFieldMap().get(AnnoFieldCache.getFieldBySqlColumn(clazz,fieldName).getName()).isEditCanClear();
        };
        WoodConfig.onExecuteBef((cmd) -> {
            if (AnnoContextUtil.hasContext()) {
                ReentrantStopWatch stopWatch = AnnoContextUtil.getContext().getStopWatch();
                String taskName;
                if (CollUtil.isEmpty(cmd.paramMap())) {
                    taskName = cmd.text;
                } else {
                    taskName = String.format("%s\n%s params ==> %s", cmd.text, " ".repeat(16), cmd.paramMap());
                }
                stopWatch.start(taskName);
            }
            return true;
        });
        WoodConfig.onExecuteAft((cmd) -> {
            if (AnnoContextUtil.hasContext()) {
                ReentrantStopWatch stopWatch = AnnoContextUtil.getContext().getStopWatch();
                stopWatch.stop();
            }
        });

        WoodConfig.namingStrategy = new NamingStrategy(){
            @Override
            public String classToTableName(Class<?> clz) {
                // TODO 后续可解绑Wood
                return super.classToTableName(clz);
            }

            @Override
            public String fieldToColumnName(Class<?> clz, Field f) {
                try {
                    return AnnoFieldCache.getSqlColumnByField(clz, f);
                }catch (Exception ignore){
                    return super.fieldToColumnName(clz, f);
                }
            }
        };

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
