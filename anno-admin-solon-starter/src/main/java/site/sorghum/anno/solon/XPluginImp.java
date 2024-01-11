package site.sorghum.anno.solon;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import org.noear.dami.Dami;
import org.noear.dami.DamiConfig;
import org.noear.dami.bus.impl.RoutingPath;
import org.noear.dami.bus.impl.TopicRouterPatterned;
import org.noear.solon.Solon;
import org.noear.solon.core.*;
import org.noear.solon.web.staticfiles.StaticMappings;
import org.noear.solon.web.staticfiles.repository.ClassPathStaticRepository;
import org.noear.wood.WoodConfig;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.AnnoConstants;
import site.sorghum.anno._common.exception.BizException;
import site.sorghum.anno._metadata.AnEntity;
import site.sorghum.anno._metadata.AnField;
import site.sorghum.anno._metadata.MetadataContext;
import site.sorghum.anno._metadata.MetadataManager;
import site.sorghum.anno.anno.annotation.clazz.AnnoMain;
import site.sorghum.anno.anno.annotation.global.AnnoScan;
import site.sorghum.anno.anno.dami.DamiApiCached;
import site.sorghum.anno.anno.dami.TopicDispatcherMonitor;
import site.sorghum.anno.anno.util.AnnoClazzCache;
import site.sorghum.anno.anno.util.AnnoFieldCache;
import site.sorghum.anno.anno.util.AnnoUtil;
import site.sorghum.anno.i18n.I18nUtil;
import site.sorghum.anno.solon.init.InitDdlAndDataService;
import site.sorghum.anno.solon.interceptor.WoodSqlLogInterceptor;

import javax.sql.DataSource;
import java.util.*;

/**
 * Solon Anno-Admin 插件
 *
 * @author sorghum
 * @since 2023/05/20
 */
public class XPluginImp implements Plugin {
    private static final String ANNO_BASE_PACKAGE = "site.sorghum.anno";
    private static final String PLUGIN_BASE_PACKAGE = "site.sorghum.plugin";
    @Override
    public void start(AppContext context) throws Throwable {

        AnnoBeanUtils.setBean(new SolonBeanImpl(context));

        i18nSupport();

        // 扫描包
        context.beanScan(ANNO_BASE_PACKAGE);
        Class<?> source = Solon.app().source();
        Set<String> packages = CollUtil.newHashSet(source.getPackage().getName(), ANNO_BASE_PACKAGE);
        AnnoScan annotation = AnnotationUtil.getAnnotation(source, AnnoScan.class);
        if (Objects.nonNull(annotation)) {
            String[] value = annotation.scanPackage();
            if (value.length > 0) {
                packages.addAll(CollUtil.newArrayList(value));
            }
        }

        // dami 配置项
        DamiConfig.configure(new TopicRouterPatterned<>(RoutingPath::new));
        DamiConfig.configure(new DamiApiCached(Dami::bus));
        DamiConfig.configure(new TopicDispatcherMonitor<>());

        List<MetadataContext> metadataContextList = context.getBeansOfType(MetadataContext.class);
        for (MetadataContext metadataContext : metadataContextList) {
            Dami.api().registerListener(MetadataManager.METADATA_TOPIC, metadataContext);
        }

        // 加载 anno 元数据
        loadMetadata(context, packages);

        // 前端静态文件
        StaticMappings.add(AnnoConstants.BASE_URL + "/", new ClassPathStaticRepository("/WEB-INF/anno-admin-ui/"));

        // 优先 初始化数据库表结构和预置数据，其他模块在创建 bean 时，可能会查库
        context.getBeanAsync(InitDdlAndDataService.class, initDdlAndDataService -> {
            context.getBeanAsync(DataSource.class, dataSource -> {
                try {
                    initDdlAndDataService.initDdl();
                } catch (Throwable e) {
                    throw new BizException(e);
                }
            });
        });

        // 扫描翻译插件
        context.beanScan(PLUGIN_BASE_PACKAGE);

        // WOOD
        WoodConfig.onExecuteAft(new WoodSqlLogInterceptor());
    }

    /**
     * 加载 anno 元数据
     */
    private void loadMetadata(AppContext context, Set<String> packages) {
        MetadataManager metadataManager = context.getBean(MetadataManager.class);
        HashSet<Class<?>> classSet = new HashSet<>();
        // 所有类
        for (String scanPackage : packages) {
            classSet.addAll(ClassUtil.scanPackage(scanPackage));
        }
        for (Class<?> clazz : classSet) {
            if (clazz.isInterface()) {
                continue;
            }
            // annoMain 主注释
            AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
            if (annoMain != null) {
                // 加载anEntity
                AnEntity anEntity = metadataManager.loadEntity(clazz);
                // 缓存处理类
                AnnoClazzCache.put(clazz.getSimpleName(), clazz);
                // 缓存字段信息
                for (AnField field : anEntity.getFields()) {
                    String columnName = field.getTableFieldName();
                    AnnoFieldCache.putFieldName2FieldAndSql(clazz, columnName, field.getFieldName());
                    // 同时保存其实际节点的类的字段信息
                    if (clazz != field.getDeclaringClass()) {
                        AnnoFieldCache.putFieldName2FieldAndSql(field.getDeclaringClass(), columnName, field.getFieldName());
                    }
                }
            }

        }
    }


    public void i18nSupport() {
        I18nUtil.setI18nService(org.noear.solon.i18n.I18nUtil::getMessage);
    }

}
