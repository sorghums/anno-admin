package site.sorghum.anno.solon;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.noear.dami.Dami;
import org.noear.dami.DamiConfig;
import org.noear.dami.bus.impl.RoutingPath;
import org.noear.dami.bus.impl.TopicRouterPatterned;
import org.noear.solon.Solon;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.BeanBuilder;
import org.noear.solon.core.BeanInjector;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.VarHolder;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.util.ProxyBinder;
import org.noear.solon.data.tran.TranExecutor;
import org.noear.solon.data.tran.TranExecutorDefault;
import org.noear.solon.web.staticfiles.StaticMappings;
import org.noear.solon.web.staticfiles.repository.ClassPathStaticRepository;
import site.sorghum.anno._annotations.Primary;
import site.sorghum.anno._annotations.Proxy;
import site.sorghum.anno._common.AnnoBeanUtils;
import site.sorghum.anno._common.exception.BizException;
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
import site.sorghum.anno.solon.init.InitDdlAndDateService;
import site.sorghum.anno.solon.interceptor.TransactionalInterceptor;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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

        context.beanInjectorAdd(Inject.class, new InjectBeanInjector(context));

        context.beanBuilderAdd(Named.class, new NamedBeanBuilder(context));

        tranSupport(context);

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
        StaticMappings.add("/", new ClassPathStaticRepository("/WEB-INF/anno-admin-ui/"));

        // 优先 初始化数据库表结构和预置数据，其他模块在创建 bean 时，可能会查库
        context.getBeanAsync(InitDdlAndDateService.class, initDdlAndDateService -> {
            context.getBeanAsync(DataSource.class, dataSource -> {
                try {
                    initDdlAndDateService.initDdl();
                } catch (Throwable e) {
                    throw new BizException(e);
                }
            });
        });

        // 扫描翻译插件
        context.beanScan(PLUGIN_BASE_PACKAGE);
    }

    /**
     * 缓存和事务支持
     */
    private static void tranSupport(AppContext context) {
        // 添加事务控制支持，see: org.noear.solon.data.integration.XPluginImp
        if (Solon.app().enableTransaction()) {
            context.wrapAndPut(TranExecutor.class, TranExecutorDefault.global);
            context.beanInterceptorAdd(Transactional.class, new TransactionalInterceptor(), 121);
        }
    }

    /**
     * 加载 anno 元数据
     */
    private void loadMetadata(AppContext context, Set<String> packages) {
        MetadataManager metadataManager = context.getBean(MetadataManager.class);
        HashSet<Class<?>> classSet = new HashSet<>();
        for (String scanPackage : packages) {
            classSet.addAll(ClassUtil.scanPackage(scanPackage));
        }
        for (Class<?> clazz : classSet) {
            if (clazz.isInterface()) {
                continue;
            }
            AnnoMain annoMain = AnnoUtil.getAnnoMain(clazz);
            if (annoMain != null) {
                metadataManager.loadEntity(clazz);
                // 缓存处理类
                AnnoClazzCache.put(clazz.getSimpleName(), clazz);
                // 缓存字段信息
                AnnoUtil.getAnnoFields(clazz).forEach(
                    field -> {
                        String columnName = AnnoUtil.getColumnName(field);
                        AnnoFieldCache.putFieldName2FieldAndSql(clazz, columnName, field.getField());
                    }
                );
            }

        }
    }

    static class InjectBeanInjector implements BeanInjector<Inject> {

        public InjectBeanInjector(AppContext context) {
            this.context = context;
        }

        AppContext context;

        @Override
        public void doInject(VarHolder varH, Inject anno) {
            Annotation[] annotations = varH.getAnnoS();
            String beanName = Optional.ofNullable(annotations).stream().flatMap(Arrays::stream)
                .filter(annotation -> annotation.annotationType().equals(Named.class))
                .findFirst()
                .map(annotation -> ((Named) annotation).value()).orElse("");
            context.beanInject(varH, beanName);
        }
    }

    static class NamedBeanBuilder implements BeanBuilder<Named> {

        public NamedBeanBuilder(AppContext context) {
            this.context = context;
        }

        AppContext context;

        @Override
        public void doBuild(Class<?> clz, BeanWrap bw, Named anno) throws Throwable {
            if (StrUtil.isNotBlank(anno.value())) {
                ReflectUtil.setFieldValue(bw, "name", anno.value());
            }

            // 是否是typed
            Primary primary = clz.getAnnotation(Primary.class);
            boolean typed = Optional.ofNullable(primary).map(Primary::value).orElse(true);
            ReflectUtil.setFieldValue(bw, "typed", typed);

            // see: AppContext#beanComponentized
            //尝试提取函数并确定自动代理
            context.beanExtractOrProxy(bw);
            //添加bean形态处理
            context.beanShapeRegister(bw.clz(), bw, bw.clz());
            //注册到容器
            context.beanRegister(bw, bw.name(), bw.typed());
            //单例，进行事件通知
            if (bw.singleton()) {
                EventBus.publish(bw.raw()); //@deprecated
                context.wrapPublish(bw);
            }

            // 生成代理类
            Proxy annotation = clz.getAnnotation(Proxy.class);
            if (annotation != null) {
                ProxyBinder.global().binding(bw);
            }

        }
    }

    public void i18nSupport() {
        I18nUtil.setI18nService(org.noear.solon.i18n.I18nUtil::getMessage);
    }

}
